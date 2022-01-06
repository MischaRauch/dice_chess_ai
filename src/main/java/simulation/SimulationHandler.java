package simulation;

import logic.player.AIPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationHandler {

    //private final AIPlayer white, black;
    private final SimulatorSingleGame game;
    private final SimulatorState states;
    public List<String> trackedStates = new ArrayList<String>();

    //boolean switch for kpis
    boolean alg = true;
    boolean algTwo = true;
    boolean winner = true;
    boolean numTurns = true;
    boolean timePerMoveWhite = true;
    boolean timePerMoveBlack = true;
    boolean numberOfPieceType = false;
    boolean numberOfPiecesPerPlayer = true;
    boolean valueOfPiecesSummed = true;
    boolean totalTime = true;
    boolean whitePiecesCaptured = false;
    boolean blackPiecesCaptured = false;

    //These are only for stateSimulator
    boolean numCaptures = false;
    boolean turn = true;
    boolean timePerMove = true;
    boolean whitePiecesRemaining = false;
    boolean blackPiecesRemaining = false;



    public SimulationHandler(AIPlayer white, AIPlayer black, String FEN) {
        game = new SimulatorSingleGame(white, black, FEN);
        states = new SimulatorState(white, black, FEN);
    }

    public void startHandler() {
        //TODO: ask user which KPI's to track or track all
        //add header row for kpis
        addHeaderRow();

        ArrayList<String> actualStats = (game.start(winner, numTurns, totalTime, timePerMoveWhite, timePerMoveBlack, numberOfPieceType, numberOfPiecesPerPlayer, valueOfPiecesSummed));

        ArrayList<String> concatenated = new ArrayList<String>();
        concatenated.addAll(actualStats);
        System.out.println("Tracked States ");
        System.out.println(concatenated);

        //Writing Single Game to Csv
        OutputToCsv writer = OutputToCsv.getInstance("singleGame.csv");
        writer.setTrackedStates(trackedStates);
        writer.writeToFileGame(concatenated); //concatenated now only has the actual states

        //Writing Each State of Game to Csv
        states.setTimeperMoveWhite(game.getTimeperMoveWhite());
        states.setTimeperMoveBlack(game.getTimeperMoveBlack());
        states.setNumTurns(game.getNumTurns());
        ArrayList<String> statesStats = (states.startStateSimulation(turn, timePerMove, numCaptures, whitePiecesRemaining, blackPiecesRemaining));
        System.out.println("States Array");
        System.out.println(statesStats);
        OutputToCsv writer1 = new OutputToCsv("statesGame.csv");
        writer1.writeEachState(statesStats);


    }

    public void addHeaderRow() {
        boolean[] booleanList = {alg, algTwo, winner, numTurns, totalTime, timePerMoveWhite, timePerMoveBlack, numberOfPieceType, numberOfPiecesPerPlayer, valueOfPiecesSummed};
        String[] header = {"Alg", "AlgTwo", "TotalTime", "TimePerMoveWhite", "TimePerMoveBlack", "Winner", "Turns", "NumberOfPieceType", "NumberOfPiecesPerPlayer", "ValueOfPiecesSummed"};

        System.out.println(Arrays.toString(booleanList));
        System.out.println(Arrays.toString(header));
        for (int i = 0; i < booleanList.length; i++) {
            if (booleanList[i]) {
                trackedStates.add(header[i]);
            }
        }
        System.out.println(trackedStates);
    }
}
