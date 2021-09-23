package logic.board;

import logic.Move;
import logic.Side;

public abstract class Board {

    public static void main(String[] args) {
        //for testing purposes
        Board b = new Board0x88(Board0x88.openingFEN);


        Board afterVariousMoves = b.movePiece("e2", "e4")
                .movePiece("b8", "c6")
                .movePiece("b2", "b4")
                .movePiece("c6", "b4");

        System.out.println(afterVariousMoves.isFriendly(afterVariousMoves.getPieceAt(Square.a1), Side.BLACK));
        System.out.println(afterVariousMoves.getPieceAt(Square.f7).isFriendly(Side.BLACK));

        System.out.println(afterVariousMoves.isEmpty(Square.b2));

        Move move = new Move(Piece.WHITE_PAWN, Square.c2, Square.c3, 1, Side.WHITE);
        afterVariousMoves.movePiece(move.origin, move.destination);

        System.out.println("Square above A1: " + afterVariousMoves.getSquareAbove(Square.a1));
        System.out.println("Piece on square above A1: " + afterVariousMoves.getPieceAt(Square.a1.getSquareAbove()));
        System.out.println("Square number of square above A1: " + Square.a1.getSquareAbove().getSquareNumber());
        System.out.println("Board index of square above A1: " + Square.a1.getSquareAbove().getBoardIndex());

        System.out.println("Get square (3, 4) by coordinate (0-indexed): " + Square.getSquare(3, 4));
        System.out.println("Piece at square (7, 3) by coordinate (0-indexed): " + afterVariousMoves.getPieceAt(Square.getSquare(7, 3)));

        Board c = new Board0x88();
    }


    public abstract boolean isEmpty(Square square);

    public abstract boolean isOffBoard(int squareNumber);

    public abstract Piece getPieceAt(Square square);

    public abstract Board movePiece(Square origin, Square destination);

    public Square getSquareAbove(Square square) {
        return Square.getSquare(square.squareNumber + 16);
    }

    public Square getSquareBelow(Square square) {
        return Square.getSquare(square.squareNumber - 16);
    }

    public Square[] getRank(int rank) {
        return new Square[0];
    }

    public Square[] getFile(String file) {
        return new Square[0];
    }

    public Square[] getDiagonals(Square square) {
        return new Square[0];
    }

    public boolean isFriendly(Piece p, Piece o) {
        return p.getColor() == o.getColor();
    }

    public boolean isFriendly(Piece piece, Side side) {
        return piece.isFriendly(side);
    }

    public abstract Board movePiece(String origin, String destination);

    public abstract Board loadFromFEN(String FEN);

    public abstract void printBoard(boolean full);


}
