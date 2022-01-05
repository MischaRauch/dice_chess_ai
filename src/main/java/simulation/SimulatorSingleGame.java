package simulation;

import logic.Move;
import logic.State;
import logic.enums.Validity;
import logic.game.Game;
import logic.player.AIPlayer;

import java.util.ArrayList;


public class SimulatorSingleGame extends Game {


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

    public ArrayList<String> start(boolean winner, boolean numTurns, boolean timePerMove, boolean numberOfPieceType, boolean numberOfPiecesPerPlayer, boolean valueOfPiecesSummed) {
        boolean gameOver = false;
        AIPlayer nextPlayer = white;
        ArrayList<String> stats = new ArrayList<String>();
        stats.add(getAIPlayerWhite().getNameAi());
        stats.add(getAIPlayerBlack().getNameAi());

        if (timePerMove) {
            stats.add("z");
        }

        while (!gameOver) {
            Move move = nextPlayer.chooseMove(currentState);

            State newState = currentState.applyMove(move);

            previousStates.push(currentState);
            checkGameOver(move);
            // after checking if king was captured, we can update the currentState
            currentState = newState;

            move.setStatus(Validity.VALID);

            processCastling();

            //update the value for gameOver, updates gameDone in Game, so we eventually exit this loop
            gameOver = isGameOver();

            //print board for debugging
            //this.getCurrentState().getBoard().printBoard();

            //get time needed for move
            if (timePerMove) {
                System.out.println(nextPlayer.getTimeNeeded());
                stats.add(Long.toString(nextPlayer.getTimeNeeded()));
            }

            //switch players
            nextPlayer = (nextPlayer == white) ? black : white;

            if (gameOver && timePerMove) {
                stats.add("z");
            }


        }
        //Save the information for this game
        if (winner) {
            tmp = getWinner().name();
            stats.add(tmp);
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

