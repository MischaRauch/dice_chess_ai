package logic.board;

public abstract class Board {

    public static void main(String[] args) {
        //for testing purposes
        Board b = new Board0x88(Board0x88.openingFEN);

        Board afterVariousMoves = b.movePiece("e2", "e4")
                .movePiece("b8", "c6")
                .movePiece("b2", "b4")
                .movePiece("c6", "b4");

        System.out.println(afterVariousMoves.isFriendly("b4", Side.BLACK));
        System.out.println(afterVariousMoves.getPieceAt("f2").isFriendly(Side.BLACK));

        System.out.println(afterVariousMoves.isEmpty(Square.b2));
        System.out.println(afterVariousMoves.isEmpty("b2"));

        Move move = new Move(Piece.WHITE_PAWN, Square.c2, Square.c3, 1, Side.WHITE);
        afterVariousMoves.movePiece(move.origin, move.destination);

        System.out.println("Square above A1: " + Square.a1.getSquareAbove());
        System.out.println("Piece on square above A1: " + afterVariousMoves.getPieceAt(Square.a1.getSquareAbove()));
        System.out.println("Square number of square above A1: " + Square.a1.getSquareAbove().getSquareNumber());
        System.out.println("Board index of square above A1: " + Square.a1.getSquareAbove().getBoardIndex());

        System.out.println("Get square (3, 4) by coordinate (0-indexed): " + Square.getSquare(3, 4));
        System.out.println("Piece at square (3, 4) by coordinate (0-indexed): " + afterVariousMoves.getPieceAt(Square.getSquare(3, 4)));
    }

    public abstract boolean isEmpty(String square);

    public abstract boolean isEmpty(Square square);

    public abstract boolean isOffBoard(int squareNumber);

    public abstract Piece getPieceAt(String square);

    public abstract Piece getPieceAt(Square square);

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

    public abstract Board movePiece(Square origin, Square destination);

    public abstract Board loadFromFEN(String FEN);

    public abstract void printBoard(boolean full);


}
