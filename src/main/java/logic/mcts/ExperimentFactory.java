package logic.mcts;

import logic.Config;
import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;
import logic.player.AIPlayer;
import logic.player.MiniMaxPlayer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static logic.enums.Piece.*;
import static logic.enums.Side.*;

public class ExperimentFactory {

    static long inSeconds = (long) 1e9;

    public static void main(String[] args) {
        //AIPlayer mcts = new MCTSAgent(WHITE, 1000);
        //AIPlayer basic = new BasicAIPlayer(BLACK);
        //AIPlayer minimax = new MiniMaxPlayer(5, BLACK);

        AIPlayer white = new MCTSAgent(WHITE, 2000);
        //AIPlayer black = new MiniMaxPlayer(7, BLACK);
        AIPlayer black = new MiniMaxPlayer(7, BLACK);
        //AIPlayer black = new BasicAIPlayer(BLACK);
        //AIPlayer black = new RandomMovesPlayer(BLACK);

        /*
        GameSimulator ssg = new GameSimulator(white, black)
                .trackExpectedValue()
                .trackDepth()
                .trackExploitation();

        ssg.start();

         */


        SimulationFactory sim = SimulationFactory.create(white, black, 300)
                .trackWinRate()
                .trackWinTotal()
                .trackTimeNeed()
                .trackTreeSize()
                .trackWinner();

        sim.start();

        System.out.println("\n###########< End Results >##############\n");
        System.out.println(sim.white.getNameAi() + " - White win average: " + sim.whiteWinTotal / sim.numSimulations);
        System.out.println(sim.black.getNameAi() + " - Black win average: " + sim.blackWinTotal / sim.numSimulations);
        System.out.println("Draws: " + sim.numDraws);
        System.out.println("Win Total: " + sim.whiteWinTotal);
        System.out.println("Min Time: " + sim.minTime);
        System.out.println("Avg Time: " + sim.averageTime);
        System.out.println("Max Time: " + sim.maxTime);
        System.out.println("Avg Tree: " + sim.avgTreeSize);
        System.out.println("Min Tree: " + sim.minTreeSize);
        System.out.println("Max Tree: " + sim.maxTreeSize);
        System.out.println("MCTS:\n choseMostVisited: " + ((MCTSAgent) sim.white).numMostVisitedChosen);
        System.out.println("      choseBestLocal: " + ((MCTSAgent) sim.white).numBestLocalChosen);
        System.out.println("      choseBestExpected: " + ((MCTSAgent) sim.white).numBestExpectedChosen);
        System.out.println("\n########################################");

    }

    static class SimulationFactory {
        AIPlayer white;
        AIPlayer black;

        double numSimulations;
        double whiteWinTotal = 0;
        double blackWinTotal = 0;
        int numDraws = 0;
        int minTreeSize = Integer.MAX_VALUE;
        int maxTreeSize = Integer.MIN_VALUE;
        int sumTreeSize = 0;
        double avgTreeSize;
        double minTime = Double.MAX_VALUE;
        double maxTime = Double.MIN_VALUE;
        double sumTime = 0;
        double averageTime = 0;


        XYSeries winTotal;
        XYSeries winRate;
        XYSeries timeNeeded;
        XYSeries timeNeededNormalized;
        XYSeries treeSize;
        XYSeries gameResult;

        List<XYSeries> plots;

        public SimulationFactory(AIPlayer white, AIPlayer black, int numSimulations) {
            this.numSimulations = numSimulations;
            this.white = white;
            this.black = black;
            plots = new LinkedList<>();
        }

        public static SimulationFactory create(AIPlayer white, AIPlayer black, int numSimulations) {
            return new SimulationFactory(white, black, numSimulations);
        }

        public SimulationFactory trackWinRate() {
            winRate = new XYSeries("Win Rate");
            plots.add(winRate);
            return this;
        }

        public SimulationFactory trackWinTotal() {
            winTotal = new XYSeries("Win Total");
            plots.add(winTotal);
            return this;
        }

        public SimulationFactory trackTimeNeed() {
            timeNeeded = new XYSeries("Time Needed");
            plots.add(timeNeeded);
            return this;
        }

        public SimulationFactory trackTreeSize() {
            treeSize = new XYSeries("Tree Size");
            plots.add(treeSize);
            return this;
        }

        public SimulationFactory trackWinner() {
            gameResult = new XYSeries("Game Result");
            plots.add(gameResult);
            return this;
        }

        public void start() {
            timeNeededNormalized = new XYSeries("Time Needed x Tree Size");
            for (int i = 1; i <= numSimulations; i++) {
                //white = new MCTSAgent(WHITE, 1000);
                //black = new BasicAIPlayer(BLACK);
                GameSimulator sim = new GameSimulator(white, black);
                sim.start();

                if (sim.victor == WHITE) whiteWinTotal++;
                else if (sim.victor == BLACK) blackWinTotal++;
                else numDraws++;

                int gameTreeSize = ((MCTSAgent) sim.white).tree.getTreeNodes().size();
                minTreeSize = Math.min(gameTreeSize, minTreeSize);
                maxTreeSize = Math.max(gameTreeSize, maxTreeSize);
                sumTreeSize += gameTreeSize;
                avgTreeSize = sumTreeSize / (double) i;
                minTime = Math.min(minTime, white.getTimeNeeded() / (double) inSeconds);
                maxTime = Math.max(maxTime, white.getTimeNeeded() / (double) inSeconds);
                sumTime += (((double) white.getTimeNeeded()) / (double) inSeconds);
                averageTime = sumTime / (double) i;

                winTotal.add(i, whiteWinTotal);
                winRate.add(i, whiteWinTotal / i);
                timeNeeded.add(i, (((double) white.getTimeNeeded()) / inSeconds));

                timeNeededNormalized.add(i, (((double) white.getTimeNeeded()) / (long) 1e5));
                treeSize.add(i, gameTreeSize);
                gameResult.add(i, (sim.victor == WHITE) ? 1 : -1);

                System.out.print(sim.victor.asChar() + " ");

                if (sim.debug) {
                    System.out.println("--------------");
                    System.out.println(white.getNameAi() + " - White win average: " + whiteWinTotal / ((double) i));
                    System.out.println(black.getNameAi() + " - Black win average: " + blackWinTotal / ((double) i));
                    System.out.println("Draws: " + numDraws);
                    System.out.println("--------------");
                }
            }

            //plot();
            plot(timeNeeded, winRate, gameResult);
            plot(timeNeededNormalized, treeSize, gameResult);
        }

        public void plot(XYSeries s1, XYSeries s2, XYSeries s3) {
            List<XYSeries> dataSet = new LinkedList<>();
            dataSet.add(s1);
            dataSet.add(s2);
            dataSet.add(s3);
            SimulationPlotter multiPlot = new SimulationPlotter("Title: " + white.getNameAi() + " v " + black.getNameAi(), dataSet, "idk");
            multiPlot.pack();
            multiPlot.setVisible(true);
        }

        public void plot() {

//            for (XYSeries data : plots) {
//                final SimulationPlotter dataPlot = new SimulationPlotter(data.getDescription() +": "+ white.getNameAi() + " vs " + black.getNameAi(), data, data.getDescription());
//                dataPlot.pack();
//                //RefineryUtilities.centerFrameOnScreen(demo);
//                dataPlot.setVisible(true);
//            }

            boolean multi = true;
            if (multi) {
                SimulationPlotter multiPlot = new SimulationPlotter("Tree & Time & Result: " + white.getNameAi() + " v " + black.getNameAi(), plots, "idk");
                multiPlot.pack();
                multiPlot.setVisible(true);
            } else {
                final SimulationPlotter winTotalPlot = new SimulationPlotter("Win Rate: " + white.getNameAi() + " vs " + black.getNameAi(), winRate, "Win Rate");
                winTotalPlot.pack();
                winTotalPlot.setVisible(true);

                final SimulationPlotter winRatePlot = new SimulationPlotter("Win Total: " + white.getNameAi() + " vs " + black.getNameAi(), winTotal, "Win Total");
                winRatePlot.pack();
                winRatePlot.setVisible(true);

                final SimulationPlotter timeNeededPlot = new SimulationPlotter("Time Needed: " + white.getNameAi() + " vs " + black.getNameAi(), timeNeeded, "Time Needed");
                timeNeededPlot.pack();
                timeNeededPlot.setVisible(true);

                final SimulationPlotter treeSizePlot = new SimulationPlotter("Tree Size: " + white.getNameAi() + " vs " + black.getNameAi(), treeSize, "Tree Size");
                treeSizePlot.pack();
                treeSizePlot.setVisible(true);

                final SimulationPlotter gamesResultPlot = new SimulationPlotter("Game Results: " + white.getNameAi() + " vs " + black.getNameAi(), gameResult, "Win or Loss");
                gamesResultPlot.pack();
                gamesResultPlot.setVisible(true);
            }
        }
    }

    static class GameSimulator extends Game {

        boolean debug = false;
        AIPlayer white, black, currentPlayer;
        Side victor;

        List<String> keys;
        HashMap<String, XYSeries> dataSeries;

        public GameSimulator(AIPlayer white, AIPlayer black) {
            super(Config.OPENING_FEN);
            this.white = white;
            this.black = black;
            currentPlayer = white;
            keys = new LinkedList<>();
            dataSeries = new HashMap<>();
            series = new LinkedList<>();
        }

        public void start() {
            while (!gameDone) {
                Move move = currentPlayer.chooseMove(currentState);
                currentState = currentState.applyMove(move);
                processCastling();
                numTurns++;

                //collectData();

                gameDone = checkGameOver(currentState);
                if (gameDone) break;
                if (debug) {
                    System.out.println("================");
                    currentState.board.printBoard();
                    System.out.println("Player: " + currentPlayer.getNameAi() + "\nMoved: + " + move.toString() + "\n================");
                }
                currentPlayer = (currentPlayer == white) ? black : white;
            }

            victor = currentPlayer.getColor();

            //plot();

            if (debug) {
                System.out.println("+++++++++++++++");
                System.out.println("WINNER: " + victor);
                System.out.println("NAME: " + currentPlayer.getNameAi());
                System.out.println("TURNS: " + numTurns);
                System.out.println("+++++++++++++++");
            }
        }

        public void collectData() {
            MCTSAgent agent = (MCTSAgent) white;

//            if (trackExpectedValue) {
//                XYSeries exploit = dataSeries.get("Expected Value");
//                exploit.add(numTurns, agent.root.getExpectedValue());
//                exploitation += agent.bestChild(agent.root).getExpectedValue(); //should be UCT explore term
//            }
//
//            if (trackExploitation) {
//                XYSeries exploit = dataSeries.get("Exploitation");
//                exploit.add(numTurns, agent.root.getExpectedValue());
//                exploitation += agent.bestChild(agent.root).getExpectedValue(); //should be UCT explore term
//
//            }
//
//            if (trackExploration) {
//                XYSeries explore = dataSeries.get("Exploration");
//                explore.add(numTurns, agent.bestChild(agent.root).getExpectedValue());
//                exploration += agent.root.getExpectedValue();
//
//            }

            if (numTrackPieces) {
                XYSeries pieceData = dataSeries.get("Num Pieces");
                //for (currentPlayer.getValidMoves(currentState))
                pieceData.add(numTurns, agent.getValidMoves(currentState).size());
                pieces += agent.getValidMoves(currentState).size();

            }

            if (trackGameTree) {
                XYSeries treeSet = dataSeries.get("Tree Depth");
                treeSet.add(numTurns, agent.tree.getTreeNodes().size());
                gameTreeSize += agent.tree.getTreeNodes().size();
            }

            if (trackTimePerTurn) {
                XYSeries s = dataSeries.get("Time Needed");
                s.add(numTurns, agent.getTimeNeeded() / 1e9);
                timeNeeded += agent.getTimeNeeded();
            }
        }

        public void plot() {
            for (Map.Entry<String, XYSeries> s : dataSeries.entrySet()) {
                SimulationPlotter gamesResultPlot = new SimulationPlotter(s.getKey() + " " + white.getNameAi() + " vs " + black.getNameAi(), s.getValue(), "Win or Loss");
                gamesResultPlot.pack();
                gamesResultPlot.setVisible(true);
            }

            // System.out.println(victor);
        }

        public boolean checkGameOver(State state) {
            if (numTurns > 100) {
                victor = NEUTRAL;
                return true;
            }
            Piece[] board = state.getBoard().getBoard();
            boolean whiteKing = false, blackKing = false;

            int pieceCount = 0;
            for (Piece piece : board) {
                if (piece != OFF_BOARD && piece != EMPTY)
                    pieceCount++;
                if (piece == WHITE_KING)
                    whiteKing = true;
                else if (piece == BLACK_KING)
                    blackKing = true;

                if (whiteKing && blackKing) //both kings on the board, so not terminal
                    return false;
            }

            if (pieceCount == 2) victor = NEUTRAL; //draw
            return true; //either one of the kings is not on the board
        }

        public static GameSimulator StateSimulationFactory(AIPlayer white, AIPlayer black) {
            return new GameSimulator(white, black);
        }

        boolean trackUCT, trackExpectedValue, trackExploitation, trackExploration, trackTimePerTurn,
                trackGameTree, trackMaxDepth, trackMinDepth, trackMove, numTrackPieces;

        long timeNeeded;
        double expectedValue, exploitation, exploration;
        int numWins, gameTreeSize, maxDepth, mindDepth, pieces, numVisits;

        Action.ActionType move;

        List<XYSeries> series;

        public GameSimulator trackSeries(String key) {
            dataSeries.put(key, new XYSeries(key));
            keys.add(key);
            return this;
        }

        public GameSimulator trackUCT() {
            XYSeries uct = new XYSeries("UCT");
            dataSeries.put("UCT", uct);
            keys.add("UCT");
            trackUCT = true;
            trackExploitation = true;
            trackExploration = true;
            series.add(uct);
            return this;
        }

        public GameSimulator trackTimeNeeded() {
            XYSeries timeNeededData = new XYSeries("Time Needed");
            dataSeries.put("Time Needed", timeNeededData);
            keys.add("Time Needed");
            trackTimePerTurn = true;
            timeNeeded = 0;
            series.add(timeNeededData);
            return this;
        }

        public GameSimulator trackExpectedValue() {
            String d = "Expected Value";
            XYSeries expectedValueSeries = new XYSeries(d);
            dataSeries.put(d, expectedValueSeries);
            keys.add(d);

            trackExpectedValue = true;
            expectedValue = 0;

            series.add(numTurns, expectedValueSeries);
            return this;
        }

        public GameSimulator trackExploitation() {
            String d = "Exploitation";
            XYSeries exploitationSeries = new XYSeries(d);
            dataSeries.put(d, exploitationSeries);
            keys.add(d);

            trackExploitation = true;
            numWins = 0;
            exploitation = 0.0;

            series.add(exploitationSeries);
            return this;
        }

        public GameSimulator trackExploration() {
            String d = "Exploration";
            XYSeries explorationSeries = new XYSeries(d);
            dataSeries.put(d, explorationSeries);
            keys.add(d);

            trackExploration = true;
            numVisits = 0;
            exploration = 0.0;

            series.add(explorationSeries);
            return this;
        }

        public GameSimulator trackMaxDepth() {
            XYSeries maxDepthSeries = new XYSeries("Max Depth");
            dataSeries.put("Max Game-Tree Depth", maxDepthSeries);
            keys.add("Max Depth");
            //trackGameTree = true;
            trackMaxDepth = true;
            maxDepth = 0;
            series.add(maxDepthSeries);
            return this;
        }

        public GameSimulator trackMinDepth() {
            XYSeries minDepthSeries = new XYSeries("Min Depth");
            dataSeries.put("Min Game-Tree Depth", minDepthSeries);
            keys.add("Min Depth");
            trackMaxDepth = true;
            mindDepth = 0;
            series.add(minDepthSeries);
            return this;
        }

        public GameSimulator trackNumPieces() {
            XYSeries numPiecesSeries = new XYSeries("Num Pieces");
            dataSeries.put("Num Pieces", numPiecesSeries);
            keys.add("Num Pieces");
            numTrackPieces = true;
            pieces = 16;
            series.add(numPiecesSeries);
            return this;
        }

        public GameSimulator trackDepth() {
            XYSeries treeDepthSeries = new XYSeries("Tree Depth");
            dataSeries.put("Tree Depth", treeDepthSeries);
            keys.add("Tree Depth");
            trackGameTree = true;
            gameTreeSize = 0;
            series.add(treeDepthSeries);
            return this;
        }

    }


    static class SimulationPlotter extends ApplicationFrame {

        /**
         * A demonstration application showing an XY series containing a null value.
         *
         * @param title the frame title.
         */
        public SimulationPlotter(String title, XYSeries results, String yAxis) {
            super(title);
            final XYSeriesCollection data = new XYSeriesCollection(results);
            final JFreeChart chart = ChartFactory.createXYLineChart(
                    title,
                    "n",
                    yAxis,
                    data,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            final ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(640, 640));
            setContentPane(chartPanel);

        }

        public SimulationPlotter(String title, List<XYSeries> series, String yAxis) {
            super(title);

            XYSeriesCollection data = new XYSeriesCollection();
            for (XYSeries s : series)
                data.addSeries(s);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    title,
                    "n",
                    yAxis,
                    data,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            customizeChart(chart);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(640, 640));
            setContentPane(chartPanel);

        }

        private void customizeChart(JFreeChart chart) {   // here we make some customization
            XYPlot plot = chart.getXYPlot();
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

            // sets paint color for each series
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, Color.GREEN);
            renderer.setSeriesPaint(2, Color.YELLOW);
            renderer.setSeriesLinesVisible(2, false);

            // sets thickness for series (using strokes)
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            renderer.setSeriesStroke(1, new BasicStroke(2.0f));
            renderer.setSeriesStroke(2, new BasicStroke(3.0f));

            // sets paint color for plot outlines
            plot.setOutlinePaint(Color.BLUE);
            plot.setOutlineStroke(new BasicStroke(2.0f));

            // sets renderer for lines
            plot.setRenderer(renderer);

            // sets plot background
            plot.setBackgroundPaint(Color.DARK_GRAY);

            // sets paint color for the grid lines
            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.BLACK);

            plot.setDomainGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.BLACK);

        }

    }


    /*
    http://www.incompleteideas.net/609%20dropbox/other%20readings%20and%20resources/MCTS-survey.pdf

    Each node represents a state.
    Each edge represents an action resulting in child node state
    General MCTS approach PseudoCode:
        function MCTSearch(State0)
            create root node v0 with state0
            while withing computation budget do:
                vL = TreePolicy(v0)                 //vL is the last node reached during tree policy (Selection) phase corresponds to state sL
                delta = DefaultPolicy(s(vL))        //reward obtained from running default policy from vL (sL)
                Backup(vL, delta)
            return a(BestChild(v0))                 //return action a that leads to the best child of the root node v0 -> "best" is defined by the implantation

     UCT/UCB approach:
     The value of a child node is the expected reward approximated by the MC simulations
        -> Rewards correspond to random variables with unknown distributions
        -> unvisited children are assigned the largest possible value, to ensure that all children of a node are
            considered at least once before expanding further -> results in "powerful form of iterated local search"

     */

    /*
    page 9 - http://www.incompleteideas.net/609%20dropbox/other%20readings%20and%20resources/MCTS-survey.pdf
    UCT Algorithm:
        function UCTSearch(s0):
            create root node v0 with state s0
            while (within computational budget) do:
                vl <- TreePolicy(v0)
                delta <-  DefaultPolicy(s(vl))
                Backup(vl, delta)
            return a(BestChild(v0, 0))

        function TreePolicy(v):
            while v is non-terminal do:
                if v not full expanded then:
                    return Expand(V)
                else:
                    v <- BestChild(v, Cp)
            return v

        function Expand(v):
            choose action a from the set of untried actions from A(s(v))        //A(s(v)) legal actions from the state corresponding to node v
            add new child v' to v
                with s(v') = f(s(v), a)     //the state of the child node v' is the result of applying action a to the state of parent node v
                and a(v') = a               //nodes contain state action pairs
            return v'

        function BestChild(v, c):
            return child node v' which maximizes ( Q(v')/N(v') + c * sqrt[2*ln(N(v)) / N(v')]) //a UCT selection formula

        function DefaultPolicy(s)
            while s is non-terminal do:
                choose action a from A(s) uniformly at random
                s <- f(s, a)    //update s to be the state after applying action a from previous s
            return reward for state s

        function Backup(v, delta):


     */
}
