package logic;

import logic.board.Board0x88;
import logic.enums.Side;
import logic.enums.Validity;
import java.util.Stack;

public class Game {
    static String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";
    private static Game CURRENT_GAME;

    private final Stack<State> previousStates;
    private final Stack<State> redoStates;
    private final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    private State currentState;

    private Stack<PieceAndTurnDeathTuple> deadBlackPieces = new Stack<>();
    private Stack<PieceAndTurnDeathTuple> deadWhitePieces = new Stack<>();

    private Stack<PieceAndTurnDeathTuple> redoDeadBlackPieces = new Stack<>();
    private Stack<PieceAndTurnDeathTuple> redoDeadWhitePieces = new Stack<>();

    public Game() {
        this(openingFEN);
    }

    public Game(String initialPosition) {
        currentState = new State(new Board0x88(initialPosition), Math.random() < 0.5 ? 1 : 2, Side.WHITE);
        previousStates = new Stack<>();
        redoStates = new Stack<>();
        CURRENT_GAME = this;

    }

    public static Game getInstance() {
        return CURRENT_GAME;
    }

    // called for GUI to moves Tile
    public Move makeMove(Move move) {
        if (evaluator.isLegalMove(move, currentState)) { //move legal

            State newState = currentState.applyMove(move);

            previousStates.push(currentState);
            currentState = newState;
            move.setStatus(Validity.VALID);

        } else {
            move.setInvalid();
        }

        //send back to GUI with updated validity flag
        return move;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Side getTurn() {
        return currentState.color;
    }

    public int getDiceRoll() {
        return currentState.diceRoll;
    }

    public Stack<PieceAndTurnDeathTuple> getRedoDeadBlackPieces() {
        return redoDeadBlackPieces;
    }

    public Stack<PieceAndTurnDeathTuple> getRedoDeadWhitePieces() {
        return redoDeadWhitePieces;
    }

    public Stack<PieceAndTurnDeathTuple> getDeadBlackPieces() {
        return deadBlackPieces;
    }

    public Stack<PieceAndTurnDeathTuple> getDeadWhitePieces() {
        return deadWhitePieces;
    }

    //may need to refresh gui in order to view the change
    public void undoState() {
        if (!previousStates.isEmpty()) {
            redoStates.push(currentState);              //push current state to redo stack in case user wants to redo
            currentState = previousStates.pop();        //pop the previous state off the stack
        }
    }

    //may need to refresh gui in order to view the change
    public void redoState() {
        if (!redoStates.isEmpty()) {
            previousStates.push(currentState);          //add current state to previous states stack
            currentState = redoStates.pop();            //update the current state

        }
    }

    public Stack<State> getPreviousStates() {
        return previousStates;
    }

    public Stack<State> getRedoStates() {
        return redoStates;
    }
}
