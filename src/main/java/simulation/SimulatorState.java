package simulation;

import logic.Move;
import logic.State;
import logic.enums.Validity;
import logic.game.Game;
import logic.player.AIPlayer;

import java.util.ArrayList;

public class SimulatorState extends Game {

    private final AIPlayer white, black;
    String whitePlayer;
    String blackPlayer;
    String tmp;

    public SimulatorState(AIPlayer white, AIPlayer black, String FEN) {
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

    public ArrayList<String> startStateSimulation(boolean turn, boolean timePerMove, boolean numCaptures, boolean whitePiecesRemaining, boolean blackPiecesRemaining){
        boolean gameOver = false;
        AIPlayer nextPlayer = white;
        ArrayList<String> statsState = new ArrayList<String>();
        statsState.add(getAIPlayerWhite().getNameAi());
        statsState.add(getAIPlayerBlack().getNameAi());


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
            /*if (timePerMove) {
                System.out.println(nextPlayer.getTimeNeeded());
                stats.add(Long.toString(nextPlayer.getTimeNeeded()));
            }

            //switch players
            nextPlayer = (nextPlayer == white) ? black : white;

             */

        }

        return statsState;
    }


}
