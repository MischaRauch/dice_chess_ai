package logic.board;

public interface BoardInterface {

    boolean isEmpty(String square);

    boolean isOffBoard(int squareNumber);

    Piece getPieceAt(String square);

    String getSquareAbove(String square);

    String getSquareBelow(String square);

    String[] getRank(int rank);

    String[] getFile(String file);

    String[] getDiagonals(String square);

    boolean isFriendly(String square, Side side);

    BoardInterface movePiece(String origin, String destination);

    BoardInterface loadFromFEN(String FEN);


}
