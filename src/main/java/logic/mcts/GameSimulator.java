package logic.mcts;

import logic.Config;
import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;
import logic.player.AIPlayer;
import org.jfree.data.xy.XYSeries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static logic.enums.Piece.*;
import static logic.enums.Side.NEUTRAL;
import static logic.mcts.MCTSAgent.C;

public class GameSimulator extends Game {

    boolean debug = false;
    AIPlayer white, black, currentPlayer;
    Side victor;


    List<String> keys;
    List<XYSeries> series;
    HashMap<String, XYSeries> dataSeries;
    double actionValue = 0;
    double c;

    public GameSimulator(AIPlayer white, AIPlayer black) {
        super(Config.OPENING_FEN);
        this.white = white;
        this.black = black;
        currentPlayer = white;
        keys = new LinkedList<>();
        dataSeries = new HashMap<>();
        series = new LinkedList<>();
        c = C;
    }

    public void start() {
        debug = false;
        C = c;
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

        //plot(series);

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

        if (trackExploitation) {
            XYSeries exploitationData = dataSeries.get("Exploitation");
            exploitationData.add(numTurns, agent.child.Q / agent.child.N);
            //actionValue += agent.child.Q;
            //exploitation += agent.bestChild(agent.root).getExpectedValue(); //should be UCT explore term
        }
//
        if (trackExpectedValue) {
            XYSeries expectedValue = dataSeries.get("Expected Value");
            //System.out.println(agent.child.type);
            expectedValue.add(numTurns, agent.child.getExpectedValue());
            //exploitation += agent.bestChild(agent.root).getExpectedValue(); //should be UCT explore term
        }
//
        if (trackUCT) {
            XYSeries uct = dataSeries.get("UCT");
            uct.add(numTurns, agent.UCT(agent.child, c));
            //exploration += agent.root.getExpectedValue();
        }

        if (trackExploration) {
            dataSeries.get("Exploration").add(numTurns, (C * Math.sqrt(Math.log(agent.child.parent.N) / agent.child.N)));
        }

//            if (numTrackPieces) {
//                XYSeries pieceData = dataSeries.get("Num Pieces");
//                //for (currentPlayer.getValidMoves(currentState))
//                pieceData.add(numTurns, agent.getValidMoves(currentState).size());
//                pieces += agent.getValidMoves(currentState).size();
//
//            }
//
//            if (trackGameTree) {
//                XYSeries treeSet = dataSeries.get("Tree Depth");
//                treeSet.add(numTurns, agent.tree.getTreeNodes().size());
//                gameTreeSize += agent.tree.getTreeNodes().size();
//            }
//
        if (trackTimePerTurn) {
            XYSeries s = dataSeries.get("Time Needed");
            s.add(numTurns, agent.getTimeNeeded() / 1e9);
            timeNeeded += agent.getTimeNeeded();
        }
    }

    public void plot(List<XYSeries> dataSet) {
        SimulationPlotter multiPlot = new SimulationPlotter("Title: " + white.getNameAi() + " v " + black.getNameAi(), dataSet, "idk");
        multiPlot.pack();
        multiPlot.setVisible(true);
    }

    public void plot() {
        for (Map.Entry<String, XYSeries> s : dataSeries.entrySet()) {
            SimulationPlotter gamesResultPlot = new SimulationPlotter(s.getKey() + " " + white.getNameAi() + " vs " + black.getNameAi(), s.getValue(), s.getKey());
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

    public GameSimulator tuneC(double C) {
        c = C;
        return this;
    }

    boolean trackUCT, trackExpectedValue, trackExploitation, trackExploration, trackTimePerTurn,
            trackGameTree, trackMaxDepth, trackMinDepth, trackMove, numTrackPieces;

    long timeNeeded;
    double expectedValue, exploitation, exploration;
    int numWins, gameTreeSize, maxDepth, mindDepth, pieces, numVisits;

    Action.ActionType move;

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
        //trackExploitation = true;
        //trackExploration = true;
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

//        series.add(numTurns, expectedValueSeries);
        series.add(expectedValueSeries);
        return this;
    }

    public GameSimulator trackExploitation() {
        String k = "Exploitation";
        XYSeries exploitationSeries = new XYSeries(k);
        dataSeries.put(k, exploitationSeries);
        keys.add(k);

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
