package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SimulatorMultiGame {
    //call SimulatorSingleGame multiple times
    private final SimulatorSingleGame ssg;
    OutputToCsv writer;
    public List<String> statsForOneGame = new ArrayList<String>();
    public List<String> trackedStates = new ArrayList<String>();
    private final int numberOfSimulations;
    private final HashMap<String, Boolean> booleaList;

    public SimulatorMultiGame(SimulatorSingleGame ssg, int numberOfSimulations, HashMap<String, Boolean> booleaList, List<String> trackedStates) {
        this.ssg = ssg;
        this.numberOfSimulations = numberOfSimulations;
        this.booleaList = booleaList;
        this.trackedStates = trackedStates;
    }

    public void start() {
        createFileAndHeader();

        //perform plays and track them till the ammount of simulation is reached
        for (int i = 0; i < numberOfSimulations; i++) {

            //State state = ssg.getinitalState();
            //System.out.println("RETURN "+state);
            statsForOneGame = ssg.start(booleaList.get("Winner"), booleaList.get("Turns"), booleaList.get("TimePerMoveWhite"), booleaList.get("TimePerMoveBlack"), booleaList.get("TotalGameTime"), booleaList.get("NumberOfPiecesBlack"), booleaList.get("NumberOfPiecesBlack"), booleaList.get("ValueOfPiecesSummedWhite"), booleaList.get("ValueOfPiecesSummedBlack"));
            System.out.println("FINISHED ONE GAME");
            writer.writeToFileGame(statsForOneGame);
            System.out.println("END " + i + " " + statsForOneGame.toString());
            statsForOneGame.removeAll(statsForOneGame);

        }


    }

    public void createFileAndHeader() {
        writer = OutputToCsv.getInstance("multiGame.csv");
        writer.setTrackedStates(trackedStates);
    }
}
