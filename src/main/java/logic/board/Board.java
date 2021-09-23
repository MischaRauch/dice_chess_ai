package logic.board;

import logic.Move;
import logic.Side;

public abstract class Board {

    public static void main(String[] args) {
        //for testing purposes
        Board b = new Board0x88(Board0x88.openingFEN);
        b.printBoard(false);

        Board afterVariousMoves = b.movePiece(Square.c2, Square.c3)
                .movePiece(Square.b8, Square.c6)
                .movePiece(Square.b2, Square.b4)
                .movePiece(Square.c6, Square.b4);

        System.out.println(afterVariousMoves.isFriendly(afterVariousMoves.getPieceAt(Square.a1), Side.BLACK));
        System.out.println(afterVariousMoves.getPieceAt(Square.f7).isFriendly(Side.BLACK));

        System.out.println(afterVariousMoves.isEmpty(Square.b2));

        Move move = new Move(Piece.WHITE_PAWN, Square.c3, Square.b4, 1, Side.WHITE);
        afterVariousMoves.movePiece(move.getOrigin(), move.getDestination());

        System.out.println("Square above A1: " + afterVariousMoves.getSquareAbove(Square.a1));
        System.out.println("Piece on square above A1: " + afterVariousMoves.getPieceAt(Square.a1.getSquareAbove()));
        System.out.println("Square number of square above A1: " + Square.a1.getSquareAbove().getSquareNumber());
        System.out.println("Board index of square above A1: " + Square.a1.getSquareAbove().getBoardIndex());

        System.out.println("Get square (3, 4) by coordinate (0-indexed): " + Square.getSquare(3, 4));
        System.out.println("Piece at square (7, 3) by coordinate (0-indexed): " + afterVariousMoves.getPieceAt(Square.getSquare(7, 3)));

        Board c = new Board0x88();
        c.printBoard(true);
    }


    public abstract boolean isEmpty(Square square);

    //this is kinda only relevant for 0x88 i think
    public abstract boolean isOffBoard(int squareNumber);

    public abstract Piece getPieceAt(Square square);

    public abstract Board movePiece(Square origin, Square destination);

    //not sure if this belongs in Square enum or here. Right noe its in both plsces
    public Square getSquareAbove(Square square) {
        return Square.getSquare(square.squareNumber + 16);
    }

    //not sure if this belongs in Square enum or here. Right noe its in both plsces
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

    public abstract Board loadFromFEN(String FEN);

    public abstract void printBoard(boolean full);


}
