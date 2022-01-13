package logic.mcts;

import logic.Config;
import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;
import logic.player.AIPlayer;
import logic.player.BasicAIPlayer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.LinkedList;
import java.util.List;

import static logic.enums.Piece.*;
import static logic.enums.Side.*;

public class ExperimentFactory {

    public static void main(String[] args) {
        //AIPlayer mcts = new MCTSAgent(WHITE, 1000);
        //AIPlayer basic = new BasicAIPlayer(BLACK);
        //AIPlayer minimax = new MiniMaxPlayer(5, BLACK);

        AIPlayer white = new MCTSAgent(WHITE, 2000);
//        AIPlayer black = new MiniMaxPlayer(7, BLACK);
        AIPlayer black = new BasicAIPlayer(BLACK);
        //AIPlayer black = new RandomMovesPlayer(BLACK);


        SimulationFactory sim = SimulationFactory.create(white, black, 20)
                .trackWinRate().trackWinTotal();
        sim.start();
        System.out.println("\n###########< End Results >##############\n");
        System.out.println(sim.white.getNameAi() + " - White win average: " + sim.whiteWinTotal / sim.numSimulations);
        System.out.println(sim.black.getNameAi() + " - Black win average: " + sim.blackWinTotal / sim.numSimulations);
        System.out.println("Draws: " + sim.numDraws);
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

        XYSeries winTotal;
        XYSeries winRate;

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

        public void start() {
            for (int i = 1; i <= numSimulations; i++) {
                //white = new MCTSAgent(WHITE, 1000);
                //black = new BasicAIPlayer(BLACK);
                GameSimulator sim = new GameSimulator(white, black);
                sim.start();

                if (sim.victor == WHITE) whiteWinTotal++;
                else if (sim.victor == BLACK) blackWinTotal++;
                else numDraws++;

                winTotal.add(i, whiteWinTotal);
                winRate.add(i, whiteWinTotal / i);

                System.out.print(sim.victor.asChar() + " ");

                if (sim.debug) {
                    System.out.println("--------------");
                    System.out.println(white.getNameAi() + " - White win average: " + whiteWinTotal / ((double) i));
                    System.out.println(black.getNameAi() + " - Black win average: " + blackWinTotal / ((double) i));
                    System.out.println("Draws: " + numDraws);
                    System.out.println("--------------");
                }
            }

            plot();
        }

        public void plot() {
//            for (XYSeries data : plots) {
//                final SimulationPlotter dataPlot = new SimulationPlotter("Win Rate: "+ white.getNameAi() + " vs " + black.getNameAi(), winRate, "Win Rate");
//                dataPlot.pack();
//                //RefineryUtilities.centerFrameOnScreen(demo);
//                dataPlot.setVisible(true);
//            }
            final SimulationPlotter winTotalPlot = new SimulationPlotter("Win Rate: " + white.getNameAi() + " vs " + black.getNameAi(), winRate, "Win Rate");
            winTotalPlot.pack();
            winTotalPlot.setVisible(true);

            final SimulationPlotter winRatePlot = new SimulationPlotter("Win Total: " + white.getNameAi() + " vs " + black.getNameAi(), winTotal, "Win Total");
            winRatePlot.pack();
            //RefineryUtilities.centerFrameOnScreen(demo);
            winRatePlot.setVisible(true);
        }
    }

    static class GameSimulator extends Game {

        boolean debug = false;
        AIPlayer white, black, currentPlayer;
        Side victor;

        public GameSimulator(AIPlayer white, AIPlayer black) {
            super(Config.OPENING_FEN);
            this.white = white;
            this.black = black;
            currentPlayer = white;
        }

        public void start() {
            while (!gameDone) {
                numTurns++;
                Move move = currentPlayer.chooseMove(currentState);
                currentState = currentState.applyMove(move);
                processCastling();
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

            if (debug) {
                System.out.println("+++++++++++++++");
                System.out.println("WINNER: " + victor);
                System.out.println("NAME: " + currentPlayer.getNameAi());
                System.out.println("TURNS: " + numTurns);
                System.out.println("+++++++++++++++");
            }
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

    }

    static class SimulationPlotter extends ApplicationFrame {

        /**
         * A demonstration application showing an XY series containing a null value.
         *
         * @param title the frame title.
         */
        public SimulationPlotter(final String title, XYSeries results, String yAxis) {
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
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
            setContentPane(chartPanel);

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
