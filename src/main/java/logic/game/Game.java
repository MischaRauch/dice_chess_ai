package logic.game;

import logic.*;
import logic.board.Board0x88;
import logic.enums.Side;

import java.util.Stack;

public abstract class Game {

    protected static String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";
    protected static Game CURRENT_GAME;

    protected final Stack<State> previousStates;
    protected final Stack<State> redoStates;
    protected final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    protected State currentState;

    protected Stack<PieceAndTurnDeathTuple> deadBlackPieces = new Stack<>();
    protected Stack<PieceAndTurnDeathTuple> deadWhitePieces = new Stack<>();

    protected Stack<PieceAndTurnDeathTuple> redoDeadBlackPieces = new Stack<>();
    protected Stack<PieceAndTurnDeathTuple> redoDeadWhitePieces = new Stack<>();

    public Game() {
        this(openingFEN);
    }

    public Game(String initialPosition) {
        currentState = new State(new Board0x88(initialPosition), Math.random() < 0.5 ? 1 : 2, Side.WHITE);
        currentState.diceRoll = Dice.roll(currentState, Side.WHITE);
        previousStates = new Stack<>();
        redoStates = new Stack<>();
        CURRENT_GAME = this;

    }

    public void undoState() {
        if (!previousStates.isEmpty()) {
            redoStates.push(currentState);              //push current state to redo stack in case user wants to redo
            currentState = previousStates.pop();        //pop the previous state off the stack
        }
    }

    public void redoState() {
        if (!redoStates.isEmpty()) {
            previousStates.push(currentState);          //add current state to previous states stack
            currentState = redoStates.pop();            //update the current state

        }
    }

    public abstract Move makeHumanMove(Move move);

    public static Game getInstance() {
        return CURRENT_GAME;
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

    public Stack<State> getPreviousStates() {
        return previousStates;
    }

    public Stack<State> getRedoStates() {
        return redoStates;
    }

}
