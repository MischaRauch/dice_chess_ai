package simulation;

import logic.Move;
import logic.State;
import logic.algorithms.BoardStateEvaluator;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Validity;
import logic.game.Game;
import logic.player.AIPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;


public class SimulatorSingleGame extends Game {

    ArrayList<String> stats = new ArrayList<String>();
    ArrayList<Long> timeperMoveWhite = new ArrayList<Long>();
    ArrayList<Long> timeperMoveBlack = new ArrayList<Long>();
    ArrayList<Long> allTimesPerMove = new ArrayList<>();
    ArrayList<int[]> pieceArrayW = new ArrayList<>();
    ArrayList<int[]> pieceArrayB = new ArrayList<>();

    private final AIPlayer white, black;
    String whitePlayer;
    String blackPlayer;
    String fen;


    public SimulatorSingleGame(AIPlayer white, AIPlayer black, String FEN) {
        super(FEN);
        this.white = white;
        this.black = black;
        whitePlayer = white.getNameAi();
        blackPlayer = black.getNameAi();
        this.fen = FEN;
    }


    public AIPlayer getAIPlayerWhite() {
        return white;
    }

    public AIPlayer getAIPlayerBlack() {
        return black;
    }

    public String getFen() {
        return fen;
    }

    public ArrayList<String> start(boolean winner, boolean numTurns, boolean timePerMoveWhite, boolean timePerMoveBlack, boolean totalGameTime, boolean numberOfPieceWhite, boolean numberOfPieceBlack, boolean valueOfPiecesSummedWhite, boolean valueOfPiecesSummedBlack) {
        //public ArrayList<String> start(boolean winner, boolean numTurns, boolean timePerMoveWhite, boolean timePerMoveBlack, boolean totalGameTime, boolean numberOfPieceType, boolean numberOfPiecesPerPlayer, boolean valueOfPiecesSummed) {
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
            //Add current Piece array to arraylist
            pieceArrayW.add(currentState.getPieceAndSquare(Side.WHITE));
            pieceArrayB.add(currentState.getPieceAndSquare(Side.BLACK));

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
            allTimesPerMove.addAll(timeperMoveWhite);
            allTimesPerMove.addAll(timeperMoveBlack);
            long sum = 0;
            for (long time : allTimesPerMove)
                sum += time;
            stats.add(Long.toString(sum));
        }
        if (winner) {
            String won = getWinner().name();
            stats.add(won);
        }
        if (numTurns) {
            stats.add(Integer.toString(previousStates.lastElement().getCumulativeTurn() + 1));
        }
        if (numberOfPieceWhite) {
            stats.add(Arrays.toString(currentState.getPieceAndSquare(Side.WHITE)));
        }
        if (numberOfPieceBlack) {
            stats.add(Arrays.toString(currentState.getPieceAndSquare(Side.BLACK)));
        }
        if (valueOfPiecesSummedWhite) {
            stats.add(Integer.toString(BoardStateEvaluator.getBoardEvaluationNumber(currentState, Side.WHITE)));
        }
        if (valueOfPiecesSummedBlack) {
            stats.add(Integer.toString(BoardStateEvaluator.getBoardEvaluationNumber(currentState, Side.BLACK)));
        }


        // reset current state to first state (first state initialized in game abstract class the first time game is initialized)
        // I think this is not needed since its only a single game - but will leave it for now
        currentState = firstState;
        //this.getCurrentState().getBoard().printBoard();


        return stats;
    }

    public ArrayList<Long> getTimeperMoveWhite(){
        return timeperMoveWhite;
    }

    public ArrayList<Long> getTimeperMoveBlack(){
        return timeperMoveBlack;
    }

    public ArrayList<int[]> getPieceArrayW(){
        return pieceArrayW;
    }

    public ArrayList<int[]> getPieceArrayB(){
        return pieceArrayB;
    }


    public int getNumTurns(){
        try {
            return previousStates.lastElement().getCumulativeTurn() + 1;
        } catch(NoSuchElementException e) {
            System.out.println("NO ELEMENT");
        }
        return 0;
    }

}

