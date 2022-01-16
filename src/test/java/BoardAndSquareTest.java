import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static logic.enums.Piece.BLACK_QUEEN;
import static logic.enums.Piece.WHITE_QUEEN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardAndSquareTest {

    //BOARD TESTS

    @Test
    @DisplayName("Board - is empty true")
    void boardIsEmptyTrue() {
        Board board = new Board0x88("r2k3r/pbppqPp1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 w - - 0 1");
        assertTrue(board.isEmpty(Square.e6));
    }

    @Test
    @DisplayName("Board - is empty false")
    void boardIsEmptyFalse() {
        Board board = new Board0x88("r2k3r/pbppqPp1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 w - - 0 1");
        assertFalse(board.isEmpty(Square.e7));
    }

    @Test
    @DisplayName("Board - is OffBoard true")
    void boardIsOffBoardTrue() {
        Board board = new Board0x88("r2k3r/pbppqPp1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 w - - 0 1");
        assertFalse(board.isOffBoard(116));
    }

    @Test
    @DisplayName("Board - is OffBoard false")
    void boardIsOffBoardFalse() {
        Board board = new Board0x88("r2k3r/pbppqPp1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 w - - 0 1");
        assertTrue(board.isOffBoard(120));
    }

    @Test
    @DisplayName("Board - get Piece at")
    void boardGetPieceAt() {
        Board board = new Board0x88("r2k3r/pbppqPp1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 w - - 0 1");
        assertTrue(board.getPieceAt(Square.e7) == BLACK_QUEEN);
    }

    @Test
    @DisplayName("Board - remove Piece")
    void boardRemovePiece() {
        Board board = new Board0x88("r2k3r/pbppqPp1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 w - - 0 1");
        board.removePiece(Square.e7);
        assertTrue(board.isEmpty(Square.e7));
    }

    @Test
    @DisplayName("Board - set Piece")
    void boardSetPiece() {
        Board board = new Board0x88("r2k3r/pbppqPp1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 w - - 0 1");
        board.setPiece(WHITE_QUEEN, Square.e6);
        assertTrue(board.getPieceAt(Square.e6) == WHITE_QUEEN);
    }

    @Test
    @DisplayName("Board - move Piece")
    void boardMovePiece() {
        Board board = new Board0x88("r2k3r/pbppqPp1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 w - - 0 1");
        board = board.movePiece(Square.c3, Square.b5);
        boolean isEmpty = board.isEmpty(Square.c3);
        boolean isNotEmpty = board.isEmpty(Square.b5);
        assertTrue(isEmpty && !isNotEmpty);
    }


    //SQUARE TESTS


    @Test
    @DisplayName("Square - get Square by rank and file")
    void squareGetSquareRankFile() {
        Square square = Square.getSquare(4, 2);
        assertTrue(square == Square.c5);
    }

    @Test
    @DisplayName("Square - get Square by squareNumber")
    void squareGetSquareSquareNumber() {
        Square square = Square.getSquare(37);
        assertTrue(square == Square.f3);
    }

    @Test
    @DisplayName("Square - get Square by index")
    void squareGetSquareIndex() {
        Square square = Square.getSquareByIndex(66);
        assertTrue(square == Square.c4);
    }

    @Test
    @DisplayName("Square - get File")
    void squareGetFile() {
        Square square = Square.getSquare(4, 2);
        int file = square.getFile();
        assertTrue(file == 2);
    }

    @Test
    @DisplayName("Square - get Rank")
    void squareGetRank() {
        Square square = Square.getSquare(4, 2);
        int rank = square.getRank() - 1; //substract -1 beacuse method is not 0 indexed
        assertTrue(rank == 4);
    }

    @Test
    @DisplayName("Square - get Square Number")
    void squareGetSquareNumber() {
        Square square = Square.getSquare(4, 2); //c5
        int squareNumber = square.getSquareNumber();
        assertTrue(squareNumber == 66);
    }

    @Test
    @DisplayName("Square - get Board Index")
    void squareGetBoardIndex() {
        Square square = Square.getSquare(4, 2); //c5
        int boardIndex = square.getBoardIndex();
        assertTrue(boardIndex == 50);
    }

    @Test
    @DisplayName("Square - get Square Right")
    void squareGetSquareRight() {
        Square square = Square.getSquare(4, 2); //c5
        Square right = square.getSquareRight();
        assertTrue(right == Square.d5);
    }

    @Test
    @DisplayName("Square - get Square Left")
    void squareGetSquareLeft() {
        Square square = Square.getSquare(4, 2); //c5
        Square left = square.getSquareLeft();
        assertTrue(left == Square.b5);
    }

    @Test
    @DisplayName("Square - get Square Above")
    void squareGetSquareAbove() {
        Square square = Square.getSquare(4, 2); //c5
        Square above = square.getSquareAbove();
        assertTrue(above == Square.c6);
    }

    @Test
    @DisplayName("Square - get Square Below")
    void squareGetSquareBelow() {
        Square square = Square.getSquare(4, 2); //c5
        Square below = square.getSquareBelow();
        assertTrue(below == Square.c4);
    }

    @Test
    @DisplayName("Square - get Square RightUp")
    void squareGetSquareRightUp() {
        Square square = Square.getSquare(4, 2); //c5
        Square rightUp = square.getRightUp();
        assertTrue(rightUp == Square.d6);
    }

    @Test
    @DisplayName("Square - get Square LeftUp")
    void squareGetSquareLeftUp() {
        Square square = Square.getSquare(4, 2); //c5
        Square leftUp = square.getLeftUp();
        assertTrue(leftUp == Square.b6);
    }

    @Test
    @DisplayName("Square - get Square RightDown")
    void squareGetSquareRightDown() {
        Square square = Square.getSquare(4, 2); //c5
        Square rightDown = square.getRightDown();
        assertTrue(rightDown == Square.d4);
    }

    @Test
    @DisplayName("Square - get Square LeftDown")
    void squareGetSquareLeftDown() {
        Square square = Square.getSquare(4, 2); //c5
        Square leftDown = square.getLeftDown();
        assertTrue(leftDown == Square.b4);
    }

    @Test
    @DisplayName("Square - is OffBoard")
    void squareisOffBoard() {
        Square square = Square.getSquare(4, 2); //c5
        boolean isOffboard = square.isOffBoard(120);
        assertTrue(isOffboard);
    }

    @Test
    @DisplayName("Square - is not OffBoard")
    void squareisNotOffBoard() {
        Square square = Square.getSquare(4, 2); //c5
        boolean isOffboard = square.isOffBoard(119);
        assertFalse(isOffboard);
    }

    @Test
    @DisplayName("Square - get Left Diagonals")
    void squareGetLeftDiagonals() {
        Square square = Square.getSquare(4, 2); //c5
        List<Integer> leftDiagonals = square.getLeftDiagonals(Square.c3);
        assertTrue(leftDiagonals.size() == 6);
    }

    @Test
    @DisplayName("Square - get Right Diagonals")
    void squareGetRightDiagonals() {
        Square square = Square.getSquare(4, 2); //c5
        List<Integer> rightDiagonals = square.getRightDiagonals(Square.e5);
        assertTrue(rightDiagonals.size() == 9);
    }

}
