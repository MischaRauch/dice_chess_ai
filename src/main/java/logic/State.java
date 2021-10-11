package logic;

import logic.board.Board;
import logic.enums.CastlingRights;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.EnumSet;

public class State {

    EnumSet<CastlingRights> castleRights = EnumSet.allOf(CastlingRights.class);

    EnumSet<Piece> availableWhitePieces = EnumSet.of(Piece.WHITE_KING,Piece.WHITE_PAWN, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT, Piece.BLACK_ROOK, Piece.WHITE_QUEEN);
    EnumSet<Piece> availableBlackPieces = EnumSet.of(Piece.BLACK_KING,Piece.BLACK_PAWN, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT, Piece.BLACK_ROOK, Piece.BLACK_QUEEN);

    public Board board;
    public int diceRoll;
    public Side color;
    public static int gameOver;

    //en-passant square

    public State(Board board, int diceRoll, Side color) {
        this.board = board;
        this.diceRoll = diceRoll;
        this.color = color;
    }
    public int getGameOver() {
        return gameOver;
    }


    public State applyMove(Move move) {
        //extract castling en passant dice roll
        Board newBoard = board.movePiece(move.origin, move.destination);
        //check if king got captured
        if (board.getPieceAt(move.getDestination()) == Piece.WHITE_KING) {
            gameOver = -1;
        }
        if (board.getPieceAt(move.getDestination()) == Piece.BLACK_KING) {
            gameOver = 1;
        }
        int newRoll = Dice.roll();      //idk about this stuff
        Side nextTurn = color == Side.WHITE ? Side.BLACK : Side.WHITE;

        //update castling rights
       // newBoard.movePiece(Square.h1, Square.d4);
       // System.out.println("TEST: "+move.origin + move.destination);
        //update available pieces sets

        return new State(newBoard, newRoll, nextTurn);
    }




}
