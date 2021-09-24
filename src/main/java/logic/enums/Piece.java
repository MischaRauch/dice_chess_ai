package logic.enums;

import java.util.HashMap;
import java.util.Map;

public enum Piece {

    EMPTY('　', Side.NEUTRAL),
    OFF_BOARD('o', Side.NEUTRAL),

    WHITE_PAWN('♟', Side.WHITE),
    WHITE_KNIGHT('♞', Side.WHITE),
    WHITE_BISHOP('♝', Side.WHITE),
    WHITE_ROOK('♜', Side.WHITE),
    WHITE_QUEEN('♛', Side.WHITE),
    WHITE_KING('♚', Side.WHITE),

    BLACK_PAWN('♙', Side.BLACK),
    BLACK_KNIGHT('♘', Side.BLACK),
    BLACK_BISHOP('♗', Side.BLACK),
    BLACK_ROOK('♖', Side.BLACK),
    BLACK_QUEEN('♕', Side.BLACK),
    BLACK_KING('♔', Side.BLACK);

    char type;
    Side color;

    Piece(char type, Side color) {
        this.type = type;
        this.color = color;
    }

    public char getType() {
        return type;
    }

    public Side getColor() {
        return color;
    }

    public boolean isFriendly(Side side) {
        return color == side;
    }

    public static Piece getPieceFromChar(char c) {
        return charPieceMap.get(c);
    }

    static Map<Character, Piece> charPieceMap = new HashMap<>();

    static {
        charPieceMap.put('P', Piece.WHITE_PAWN);
        charPieceMap.put('N', Piece.WHITE_KNIGHT);
        charPieceMap.put('B', Piece.WHITE_BISHOP);
        charPieceMap.put('R', Piece.WHITE_ROOK);
        charPieceMap.put('Q', Piece.WHITE_QUEEN);
        charPieceMap.put('K', Piece.WHITE_KING);
        charPieceMap.put('p', Piece.BLACK_PAWN);
        charPieceMap.put('n', Piece.BLACK_KNIGHT);
        charPieceMap.put('b', Piece.BLACK_BISHOP);
        charPieceMap.put('r', Piece.BLACK_ROOK);
        charPieceMap.put('q', Piece.BLACK_QUEEN);
        charPieceMap.put('k', Piece.BLACK_KING);
        charPieceMap.put('o', Piece.OFF_BOARD);
    }
}