package logic;

import logic.board.Board;
import logic.enums.CastlingRights;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.sql.SQLOutput;
import java.util.EnumSet;

import static logic.enums.Piece.*;
import static logic.enums.Side.*;

public class State {

    public static int gameOver;
    public Board board;
    public int diceRoll;
    public Side color;

    public Square enPassant = Square.INVALID;

    EnumSet<CastlingRights> castleRights = EnumSet.allOf(CastlingRights.class);
    EnumSet<Piece> availableWhitePieces = EnumSet.of(WHITE_PAWN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_QUEEN, WHITE_KING);
    EnumSet<Piece> availableBlackPieces = EnumSet.of(BLACK_PAWN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_QUEEN, BLACK_KING);

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

        //check if king got captured
        if (board.getPieceAt(move.getDestination()) == WHITE_KING) {
            gameOver = -1;
        }

        if (board.getPieceAt(move.getDestination()) == BLACK_KING) {
            gameOver = 1;
        }

        int newRoll = Dice.roll();
        Side nextTurn = color == WHITE ? BLACK : WHITE;

        //update available pieces sets
        Board newBoard = board.movePiece(move.origin, move.destination);
        State nextState = new State(newBoard, newRoll, nextTurn);

        if (move.enPassantMove) {
            nextState.enPassant = move.enPassant;
        }

        if (move.enPassantCapture) {
            newBoard.removePiece(color == WHITE ? move.destination.getSquareBelow() : move.destination.getSquareAbove());
        }

        newBoard.printBoard();
        return nextState;
    }


}
