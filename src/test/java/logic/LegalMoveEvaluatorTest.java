package logic;
import logic.board.*;
import logic.board.Board;
import logic.enums.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegalMoveEvaluatorTest {
    LegalMoveEvaluator  legalmove = new LegalMoveEvaluator();
    Move move;
    State state;
    Board b = state.board;
    Square currentSquare;

    @Test
    void testIsLegalMove() {
        Boolean value= legalmove.isLegalMove(move,state);
        assertTrue(value);
    }

    @Test
    void testIsLegalPawnMove() {
        Boolean value= legalmove.isLegalPawnMove();
        assertTrue(value);
    }

    @Test
    void testIsLegalKnightMove() {
        Boolean value= legalmove.isLegalKnightMove();
        assertTrue(value);
    }

    @Test
    void testIsLegalQueenMove() {
        Boolean value= legalmove.isLegalQueenMove();
        assertTrue(value);
    }

    @Test
    void testIsLegalRookMove() {
        Boolean value= legalmove.isLegalRookMove();
        assertTrue(value);
    }

    @Test
    void testIsLegalBishopMove() {
        Boolean value= legalmove.isLegalBishopMove();
        assertTrue(value);
    }

    @Test
    void testCheckingSides() {
        Boolean value= legalmove.checkingSides(b,move,currentSquare);
        assertTrue(value);
    }

    @Test
    void testCheckSameFile() {
        Boolean value= legalmove.checkSameFile(b,move);
        assertTrue(value);
    }

    @Test
    void testCheckSameRank() {
        Boolean value= legalmove.checkSameRank(b,move);
        assertTrue(value);
    }

    @Test
    void testCheckSameDiagonal() {
        Boolean value= legalmove.checkSameDiagonal(b,move);
        assertTrue(value);
    }
}