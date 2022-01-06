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

    public ArrayList<String> start(boolean winner, boolean numTurns, boolean timePerMoveWhite, boolean timePerMoveBlack, boolean totalGameTime, boolean numberOfPieceType, boolean numberOfPiecesPerPlayer, boolean valueOfPiecesSummed) {
        boolean gameOver = false;
        AIPlayer nextPlayer = white;


        stats.add(getAIPlayerWhite().getNameAi());
        stats.add(getAIPlayerBlack().getNameAi());


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
            if (timePerMoveWhite && (nextPlayer == white)) {
                timeperMoveWhite.add(nextPlayer.getTimeNeeded());
            }
            if (timePerMoveBlack && (nextPlayer == black)) {
                timeperMoveBlack.add(nextPlayer.getTimeNeeded());
            }

            //switch players
            nextPlayer = (nextPlayer == white) ? black : white;


        }
        //Save the information for this game
        if (timePerMoveWhite) {
            Double averageWhite = timeperMoveWhite.stream().mapToLong(val -> val).average().orElse(0.0);
            stats.add(Double.toString(averageWhite));
        }
        if (timePerMoveBlack) {
            Double averageBlack = timeperMoveBlack.stream().mapToLong(val -> val).average().orElse(0.0);
            stats.add(Double.toString(averageBlack));
        }
        if (totalGameTime) {
            timeperMoveWhite.addAll(timeperMoveBlack);
            long sum = 0;
            for (long time : timeperMoveWhite)
                sum += time;
            stats.add(Long.toString(sum));
        }
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

