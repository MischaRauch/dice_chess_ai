package logic.game;

import logic.*;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.Stack;

import static logic.enums.Piece.KING;
import static logic.enums.Side.NEUTRAL;
import static logic.enums.Side.WHITE;

public abstract class Game {

    //public static String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";
    //public static String openingFEN = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 1";
    private final String FEN;
    protected static Game CURRENT_GAME;

    protected final Stack<State> previousStates = new Stack<>();
    protected final Stack<State> redoStates = new Stack<>();
    protected final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    protected State firstState;
    protected State currentState;
    //protected boolean gameOver = false;
    protected int numTurns;

    //this boolean determines if the game is finished
    public boolean gameDone = false;
    //This variable is the winner of the game, neutral by default
    public Side winner = NEUTRAL;

    //indicated is in last state a castling was performed to disable castling rights
    //for the beginning of the next move - 0 = none, 1 = shortCasltingWhite
    //2 = shortCastlingBlack, 3 = longCastlingWhite, 4 = longCastlingBlack
    private int castlingPerformed = 0;

    protected Stack<PieceAndTurnDeathTuple<Piece, Integer>> deadBlackPieces = new Stack<>();
    protected Stack<PieceAndTurnDeathTuple<Piece, Integer>> deadWhitePieces = new Stack<>();

    protected Stack<PieceAndTurnDeathTuple<Piece, Integer>> redoDeadBlackPieces = new Stack<>();
    protected Stack<PieceAndTurnDeathTuple<Piece, Integer>> redoDeadWhitePieces = new Stack<>();

    public Game(String initialPosition) {
        currentState = new State(new Board0x88(initialPosition), Math.random() < 0.5 ? 1 : 2, Side.WHITE);
        //currentState.setDiceRoll(Dice.roll(currentState, Side.WHITE));
        currentState.setDiceRoll(Dice.roll(currentState, WHITE));
        // First time game gets initialized game instance is null so make this the first state
        if (Game.getInstance() == null) {
            firstState = new State(currentState);
            //System.out.println("GAME INSTANCE NULL, FIRST STATE INITIALIZED");
            numTurns = 0;
        }
        CURRENT_GAME = this;
        this.FEN = initialPosition;
        // System.out.println("GAME const fen");
    }

    // not abstract method as  AiAi game will never use this
    public void resetCurrentStateToFirstState() {
        currentState = new State(firstState);
    }


    public void undoState() {
        if (!previousStates.isEmpty()) {
            redoStates.push(currentState);              //push current state to redo stack in case user wants to redo
            currentState = previousStates.pop();        //pop the previous state off the stack
            numTurns--;
        }
    }

    public void redoState() {
        if (!redoStates.isEmpty()) {
            previousStates.push(currentState);          //add current state to previous states stack
            currentState = redoStates.pop();            //update the current state
            numTurns++;
        }
    }

    public void checkGameOver(Move move) {
        Board board = currentState.getBoard();
        Piece destPiece = board.getPieceAt(move.getDestination());
        if ((currentState.getCumulativeTurn()>100 && currentState.getPieceAndSquare().size()==2) ||
                (currentState.getPieceAndSquare().size()==2 && (
                        ( ((Piece) currentState.getPieceAndSquare().get(0).getPiece()).getCharType()=='K' &&
                                ((Piece) currentState.getPieceAndSquare().get(1).getPiece()).getCharType()=='k'
                        ) ||
                        ( ((Piece) currentState.getPieceAndSquare().get(0).getPiece()).getCharType()=='k' &&
                                ((Piece) currentState.getPieceAndSquare().get(1).getPiece()).getCharType()=='K'
                        ))
            )){
            gameDone = true;
            winner = NEUTRAL;
        } else if (destPiece.getType() == KING) {
            System.out.println("gameDone = true");
            gameDone = true;
            winner = move.getSide();
        }
    }

    public boolean isGameOver() {
        return gameDone;
    }

    public Side getWinner() {
        return winner;
    }

    public void setGameOver(boolean newGame) { gameDone = newGame;}

    protected void processCastling() {
        //check if castling was performed
        if (currentState.isCanCastleBlack()) {
            if (currentState.getCastling() == Square.f8) {
                System.out.println("SHORT CASTLING BLACK WAS PERFROMED 09");
                castlingPerformed = 2;
            }
            if (currentState.getCastling() == Square.d8) {
                System.out.println("LONG CASTLING BLACK WAS PERFORMED 09");
                castlingPerformed = 4;
            }
            currentState.setCastling(Square.INVALID);
        }
        if (currentState.isCanCastleWhite()) {
            if (currentState.getCastling() == Square.f1) {
                System.out.println("SHORT CASTLING WHITE WAS PERFORMED 09");
                castlingPerformed = 1;
            }
            if (currentState.getCastling() == Square.d1) {
                System.out.println("LONG CASTLING WHITE WAS PERFORMED 09");
                castlingPerformed = 3;
            }
            currentState.setCastling(Square.INVALID);
        }
    }

    public static Game getInstance() {
        return CURRENT_GAME;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Side getTurn() {
        return currentState.getColor();
    }

    public int getNumTurns(){ return numTurns; }

    public void setNumTurns(int numTurns){ this.numTurns = numTurns;}

    public int getDiceRoll() {
        return currentState.getDiceRoll();
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

    public String getFEN() {
        return FEN;
    }

}