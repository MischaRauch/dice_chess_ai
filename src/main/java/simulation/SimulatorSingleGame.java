package simulation;

import logic.Move;
import logic.State;
import logic.enums.Validity;
import logic.game.Game;
import logic.player.AIPlayer;

import java.util.ArrayList;


public class SimulatorSingleGame extends Game {

    ArrayList<String> stats = new ArrayList<String>();
    ArrayList<Long> timeperMoveWhite = new ArrayList<Long>();
    ArrayList<Long> timeperMoveBlack = new ArrayList<Long>();

    private final AIPlayer white, black;
    String whitePlayer;
    String blackPlayer;
    String tmp;


    public SimulatorSingleGame(AIPlayer white, AIPlayer black, String FEN) {
        super(FEN);
        this.white = white;
        this.black = black;
        whitePlayer = white.getNameAi();
        blackPlayer = black.getNameAi();
    }

    public AIPlayer getAIPlayerWhite() {
        return white;
    }

    public AIPlayer getAIPlayerBlack() {
        return black;
    }

    public ArrayList<String> start(boolean winner, boolean numTurns, boolean totalTime, boolean numberOfPieceType, boolean numberOfPiecesPerPlayer, boolean valueOfPiecesSummed) {
        ArrayList<String> stats = new ArrayList<String>();
        stats.add(getAIPlayerWhite().getNameAi());
        stats.add(getAIPlayerBlack().getNameAi());

        if (winner) {
            tmp = getWinner().name();
            stats.add(tmp);
        }

        if(totalTime){
            int time = 0;
            stats.add(Integer.toString(time));
        }
        if (numTurns) {
            stats.add(Integer.toString(previousStates.lastElement().getCumulativeTurn() + 1));
        }


        // reset current state to first state (first state initialized in game abstract class the first time game is initialized)
        // I think this is not needed since its only a single game - but will leave it for now
        currentState = firstState;

        System.out.println("\n\n\nGameOver\n\n\n");
        System.out.println("Winner " + winner);

        String[] returnArray = new String[4];
        returnArray[0] = getAIPlayerWhite().getNameAi();
        returnArray[1] = getAIPlayerBlack().getNameAi();
        returnArray[2] = tmp;
        returnArray[3] = Integer.toString(previousStates.lastElement().getCumulativeTurn());


        return stats;
    }

}

