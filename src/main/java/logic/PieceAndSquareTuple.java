package logic;

import logic.enums.Side;

public class PieceAndSquareTuple<Piece, Square> {
    private final Piece piece;
    private final Square square;

    public PieceAndSquareTuple(Piece piece, Square square) {
        this.piece = piece;
        this.square = square;
    }
    public PieceAndSquareTuple(PieceAndSquareTuple toClone) {
        this((Piece)toClone.getPiece(),(Square)toClone.getSquare());
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