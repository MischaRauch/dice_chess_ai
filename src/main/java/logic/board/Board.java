package logic.board;

import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.EnumSet;

public abstract class Board {

    private String FEN;

    public abstract void removePiece(Square location);

    public abstract boolean isEmpty(Square square);

    //this is kinda only relevant for 0x88 i think
    public abstract boolean isOffBoard(int squareNumber);

    public abstract Piece getPieceAt(Square square);

    public abstract Board movePiece(Square origin, Square destination);

    public abstract void setPiece(Piece piece, Square destination);

    public abstract Piece[] getBoard();

    //not sure if this belongs in Square enum or here. Right now it's in both places
    public Square getSquareAbove(Square square) {
        return Square.getSquare(square.getSquareNumber() + 16);
    }

    //not sure if this belongs in Square enum or here. Right now it's in both places
    public Square getSquareBelow(Square square) {
        return Square.getSquare(square.getSquareNumber() - 16);
    }

    public Square[] getRank(int rank) {
        return new Square[0];
    }

    public EnumSet<Square> getFile(Square square) {
        EnumSet<Square> file = EnumSet.noneOf(Square.class);
        //Square[] file = new Square[8];
        for (int i = 0; i < 8; i++) {
            file.add(Square.getSquare(i, square.getFile()));
            //file[i] = Square.getSquare(i, square.getFile());
        }
        return file;
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

    public abstract void printBoard();

    public String getFEN() {
        return FEN;
    }

    public abstract String createFENFromBoard();

    public void setFEN(String FEN) {
        this.FEN = FEN;
    }
}
