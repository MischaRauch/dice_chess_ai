package logic;

import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.enums.Validity;

import java.util.LinkedList;
import java.util.Stack;

public class Game {
    static String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";
    private static Game CURRENT_GAME;

    private final Stack<State> previousStates;
    private final Stack<State> redoStates;
    private State currentState;
    private final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();

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

    public Move makeMove(Move move) {
        if (evaluator.isLegalMove(move, currentState)) {

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

    //may need to refresh gui in order to view the change
    public void undoState(State state) {
        if (!previousStates.isEmpty()) {
            redoStates.push(currentState);              //push current state to redo stack in case user wants to redo
            currentState = previousStates.pop();        //pop the previous state off the stack
        }
    }

    //may need to refresh gui in order to view the change
    public void redoState(State state) {
        if (!redoStates.isEmpty()) {
            previousStates.push(currentState);          //add current state to previous states stack
            currentState = redoStates.pop();            //update the current state
        }
    }

}
