package logic.game;

import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.*;

import java.util.Stack;

public abstract class Game {

    public static String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";
    protected static Game CURRENT_GAME;

    protected final Stack<State> previousStates;
    protected final Stack<State> redoStates;
    protected final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    protected State currentState;
    protected boolean gameOver = false;

    //indicated is in last state a castling was performed to disable castling rights
    //for the beginning of the next move - 0 = none, 1 = shortCasltingWhite
    //2 = shortCastlingBlack, 3 = longCastlingWhite, 4 = longCastlingBlack
    public static int castlingPerformed = 0;

    protected Stack<PieceAndTurnDeathTuple<Piece, Integer>> deadBlackPieces = new Stack<>();
    protected Stack<PieceAndTurnDeathTuple<Piece, Integer>> deadWhitePieces = new Stack<>();

    protected Stack<PieceAndTurnDeathTuple<Piece, Integer>> redoDeadBlackPieces = new Stack<>();
    protected Stack<PieceAndTurnDeathTuple<Piece, Integer>> redoDeadWhitePieces = new Stack<>();



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

    protected void processCastling() {
        //check if castling was performed
        if (currentState.isApplyCastling()) {
            if (currentState.castling == Square.f8) {
                System.out.println("SHORT CASTLING BLACK WAS PERFROMED 09");
                castlingPerformed = 2;
            }
            if (currentState.castling == Square.d8) {
                System.out.println("LONG CASTLING BLACK WAS PERFORMED 09");
                castlingPerformed = 4;
            }
            if (currentState.castling == Square.f1) {
                System.out.println("SHORT CASTLING WHITE WAS PERFORMED 09");
                castlingPerformed = 1;
            }
            if (currentState.castling == Square.d1) {
                System.out.println("LONG CASTLING WHITE WAS PERFORMED 09");
                castlingPerformed = 3;
            }

            currentState.castling = Square.INVALID;
        }
    }

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

    public Stack<State> getPreviousStates() {
        return previousStates;
    }

    public Stack<State> getRedoStates() {
        return redoStates;
    }

    public int getCastlingPerformed() { return castlingPerformed; }

    public void setCastlingPerformed(int casPerf) { castlingPerformed = casPerf; }

    public Stack<PieceAndTurnDeathTuple<Piece, Integer>> getRedoDeadBlackPieces() {
        return redoDeadBlackPieces;
    }

    public Stack<PieceAndTurnDeathTuple<Piece, Integer>> getRedoDeadWhitePieces() {
        return redoDeadWhitePieces;
    }

    public Stack<PieceAndTurnDeathTuple<Piece, Integer>> getDeadBlackPieces() {
        return deadBlackPieces;
    }

    public Stack<PieceAndTurnDeathTuple<Piece, Integer>> getDeadWhitePieces() {
        return deadWhitePieces;
    }


}
