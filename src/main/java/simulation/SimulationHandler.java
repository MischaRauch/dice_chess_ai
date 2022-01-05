package simulation;

import logic.player.AIPlayer;

import java.util.Arrays;

public class SimulationHandler {

    //private final AIPlayer white, black;
    private final SimulatorSingleGame game;
    private final int played = 0;


    public SimulationHandler(AIPlayer white, AIPlayer black, String FEN) {
        game = new SimulatorSingleGame(white, black, FEN);

    }
    
    public void startHandler() {
        //TODO: ask user which KPI's to track or track all
        //2 because I tested with winner and numTurns - has to be extended to user input
        String[] trackedStates;
        trackedStates = game.start();
        System.out.println("Tracked States ");
        System.out.println(Arrays.toString(trackedStates));

        //TODO: write into csv file
    }
}
