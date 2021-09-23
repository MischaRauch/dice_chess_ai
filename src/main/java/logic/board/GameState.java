package logic.board;

import logic.Dice;

import java.util.EnumSet;

public class GameState {

    enum CastlingRights {
        WHITE_QUEEN_SIDE,
        WHITE_KING_SIDE,
        BLACK_QUEEN_SIDE,
        BLACK_KING_SIDE
    }

    EnumSet<CastlingRights> castleRights = EnumSet.allOf(CastlingRights.class);
    EnumSet<Piece> availableWhitePieces = EnumSet.of(Piece.WHITE_KING,Piece.WHITE_PAWN, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT, Piece.BLACK_ROOK, Piece.WHITE_QUEEN);
    EnumSet<Piece> availableBlackPieces = EnumSet.of(Piece.BLACK_KING,Piece.BLACK_PAWN, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT, Piece.BLACK_ROOK, Piece.BLACK_QUEEN);

    Board board;
    int diceRoll;
    Side color;

    public GameState(Board board, int diceRoll, Side color) {
        this.board = board;
        this.diceRoll = diceRoll;
        this.color = color;
    }

    public GameState applyMove(Move move) {
        Board newBoard = board.movePiece(move.origin, move.destination);
        int newRoll = Dice.roll();
        Side nextTurn = color == Side.WHITE ? Side.BLACK : Side.WHITE;

        //update castling rights
        //update available pieces sets

        return new GameState(newBoard, newRoll, nextTurn);
    }




}
