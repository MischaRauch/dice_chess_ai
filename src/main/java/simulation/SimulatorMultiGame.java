package simulation;

import logic.enums.Side;
import logic.player.AIPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SimulatorMultiGame {
    //call SimulatorSingleGame multiple times
    private final SimulatorSingleGame ssg;
    OutputToCsv writer;
    OutputToCsv stateMultiWriter = new OutputToCsv("multiStates.csv");
    public List<String> statsForOneGame = new ArrayList<String>();
    public List<String> statesForOneGame = new ArrayList<String>();
    public List<String> trackedStates = new ArrayList<String>();
    private final int numberOfSimulations;
    private final HashMap<String, Boolean> booleaList;
    AIPlayer white;
    AIPlayer black;
    String fen;

    public SimulatorMultiGame(SimulatorSingleGame ssg, int numberOfSimulations, HashMap<String, Boolean> booleaList, List<String> trackedStates) {
        this.ssg = ssg;
        this.numberOfSimulations = numberOfSimulations;
        this.booleaList = booleaList;
        this.trackedStates = trackedStates;
        this.white = ssg.getAIPlayerWhite();
        this.black = ssg.getAIPlayerBlack();
        this.fen = ssg.getFen();


    }

    public void start() {
        createFileAndHeaderGame();
        int whiteWins = 0;
        int blackWins = 0;

        //perform plays and track them till the ammount of simulation is reached
        for (int i = 0; i < numberOfSimulations; i++) {

            SimulatorSingleGame inLoop = new SimulatorSingleGame(white, black, fen);
            statsForOneGame = inLoop.start(booleaList.get("Winner"), booleaList.get("Turns"), booleaList.get("TimePerMoveWhite"), booleaList.get("TimePerMoveBlack"), booleaList.get("TotalGameTime"), booleaList.get("NumberOfPiecesBlack"), booleaList.get("NumberOfPiecesBlack"), booleaList.get("ValueOfPiecesSummedWhite"), booleaList.get("ValueOfPiecesSummedBlack"));
            writer.writeToFileGame(statsForOneGame);
            //System.out.println("END " + i + " " + statsForOneGame.toString());
            if (inLoop.getWinner() == Side.WHITE) whiteWins++;
            else blackWins++;
            writeMultiStates(inLoop);
            statesForOneGame.removeAll(statesForOneGame);
            System.out.println("==========<Win Rates>===========");
            System.out.println("====   white/black: " + (whiteWins / ((double) blackWins)) + " =====");
            System.out.println("====   black/white: " + (blackWins / ((double) whiteWins)) + " =====");
            System.out.println("====   white/total: " + (whiteWins / ((double) i + 1)) + " =====");
            System.out.println("====   black/total: " + (blackWins / ((double) i + 1)) + " =====");
            System.out.println("=======<That's all Folks>=======");

        }

    }

    private void writeMultiStates(SimulatorSingleGame inLoop) {
        SimulatorState inLoopState = new SimulatorState(white, black, fen);
        inLoopState.setTimeperMoveWhite(inLoop.getTimeperMoveWhite());
        inLoopState.setTimeperMoveBlack(inLoop.getTimeperMoveBlack());
        inLoopState.setNumTurns(inLoop.getNumTurns());
        inLoopState.setCapturePieceArraysWhite(inLoop.getCapturePieceArrayW(), inLoop.getCapturePieceArrayB());
        inLoopState.setPieceArraysWhite(inLoop.getPieceArrayW(), inLoop.getPieceArrayB());
        inLoopState.setEvaluationBoards(inLoop.getEvaluationBoardW(), inLoop.getEvaluationBoardB());
        statesForOneGame = inLoopState.startStateSimulation();
        stateMultiWriter.writeEachState(statesForOneGame);
        statsForOneGame.removeAll(statsForOneGame);
    }

    /*public void startState(){
        OutputToCsv stateMultiWriter = new OutputToCsv("multiStates.csv");
        for (int i = 0; i < numberOfSimulations; i++) {
            SimulatorState inLoopState = new SimulatorState(white, black, fen);
            statesForOneGame = inLoopState.startStateSimulation();
            stateMultiWriter.writeEachState(statesForOneGame);
            System.out.println("END STATES MULTI" + i + " " + statesForOneGame.toString());
            statesForOneGame.removeAll(statesForOneGame);
        }
    }*/

    public void createFileAndHeaderGame() {
        writer = OutputToCsv.getInstance("multiGame.csv");
        writer.setTrackedStates(trackedStates);
    }

}
