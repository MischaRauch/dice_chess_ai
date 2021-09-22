package logic.board;

public abstract class Board {

    public static void main(String[] args) {

        Board b = new Board0x88(Board0x88.openingFEN);
        Board afterVariousMoves = b.movePiece("e2", "e4")
                .movePiece("b8", "c6")
                .movePiece("b2", "b4")
                .movePiece("c6", "b4");

        System.out.println(afterVariousMoves.isFriendly("b4", Side.BLACK));
        System.out.println(afterVariousMoves.getPieceAt("f2").isFriendly(Side.BLACK));
    }

    public abstract boolean isEmpty(String square);

    public abstract boolean isOffBoard(int squareNumber);

    public abstract Piece getPieceAt(String square);

    public abstract String getSquareAbove(String square);

    public abstract String getSquareBelow(String square);

    public abstract String[] getRank(int rank);

    public abstract String[] getFile(String file);

    public abstract String[] getDiagonals(String square);

    public abstract boolean isFriendly(String square, Side side);

    public boolean isFriendly(Piece piece, Side side) {
        return piece.isFriendly(side);
    }

    public abstract Board movePiece(String origin, String destination);

    public abstract Board loadFromFEN(String FEN);

    public abstract void printBoard(boolean full);


}
