package simulation;

import logic.player.AIPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationHandler {

    //private final AIPlayer white, black;
    private final SimulatorSingleGame game;
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


    public SimulationHandler(AIPlayer white, AIPlayer black, String FEN) {
        game = new SimulatorSingleGame(white, black, FEN);

    }


    public void startHandler() {
        //TODO: ask user which KPI's to track or track all
        //add header row for kpis
        addHeaderRow();

        ArrayList<String> actualStats = (game.start(winner, numTurns, timePerMoveWhite, timePerMoveBlack, numberOfPieceType, numberOfPiecesPerPlayer, valueOfPiecesSummed));

        ArrayList<String> concatenated = new ArrayList<String>();
        concatenated.addAll(trackedStates);
        concatenated.addAll(actualStats);

        System.out.println("Tracked States ");
        System.out.println(concatenated);

        //TODO: write into csv file
        OutputToCsv writer = OutputToCsv.getInstance();
        writer.writeToFile(concatenated);
    }

    public void addHeaderRow() {
        boolean[] booleanList = {alg, algTwo, winner, numTurns, timePerMoveWhite, timePerMoveBlack, numberOfPieceType, numberOfPiecesPerPlayer, valueOfPiecesSummed};
        String[] header = {"Alg", "AlgTwo", "TimePerMoveWhite", "TimePerMoveBlack", "Winner", "Turns", "NumberOfPieceType", "NumberOfPiecesPerPlayer", "ValueOfPiecesSummed"};

        System.out.println(Arrays.toString(booleanList));
        System.out.println(Arrays.toString(header));
        for (int i = 0; i < booleanList.length; i++) {
            if (booleanList[i]) {
                trackedStates.add(header[i]);
            }
        }
        trackedStates.add("z");
        System.out.println(trackedStates);
    }
}
