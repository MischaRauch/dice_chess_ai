package logic;

import logic.enums.Side;

public class PieceAndSquareTuple<Piece, Square> {
    private final Piece piece;
    private final Square square;

    public PieceAndSquareTuple(Piece piece, Square square) {
        this.piece = piece;
        this.square = square;
    }
    public Piece getPiece() {
        return piece;
    }
    public Square getSquare() {
        return square;
    }
    public String toString() {
        return piece.toString() + " " + square.toString();
    }
}