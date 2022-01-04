package simulation;

import logic.Move;
import logic.State;
import logic.enums.Validity;
import logic.game.Game;
import logic.player.AIPlayer;


public class SimulatorSingleGame extends Game {


    private final AIPlayer white, black;
    String whitePlayer;
    String blackPlayer;
    String winner;


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

    public String[] start() {
        boolean gameOver = false;
        AIPlayer nextPlayer = white;

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

            //switch players
            nextPlayer = (nextPlayer == white) ? black : white;

            //print board for debugging
            //this.getCurrentState().getBoard().printBoard();

        }
        //Save the information for this game
        winner = getWinner().name();

        // reset current state to first state (first state initialized in game abstract class the first time game is initialized)
        // I think this is not needed since its only a single game - but will leave it for now
        currentState = firstState;

        System.out.println("\n\n\nGameOver\n\n\n");
        System.out.println("Winner " + winner);

        String[] returnArray = new String[2];
        returnArray[0] = winner;
        returnArray[1] = Integer.toString(previousStates.lastElement().getCumulativeTurn());

        return returnArray;
    }

}

