package logic;
import logic.board.Board;
import logic.enums.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegalMoveEvaluatorTest {
    LegalMoveEvaluator  legalmove = new LegalMoveEvaluator();
    Move move;
    State state;
    Board b;
    Square currentSquare;

    @Test
    void testIsLegalMove(){
        boolean value = legalmove.isLegalMove(move,state);
        assertTrue(value);
    }

    @Test
    void testIsLegalPawnMove() {
        boolean value= legalmove.isLegalPawnMove();
        assertTrue(value);
    }

    @Test
    void testIsLegalKnightMove() {
        boolean value= legalmove.isLegalKnightMove();
        assertTrue(value);
    }

    @Test
    void testIsLegalQueenMove() {
        boolean value= legalmove.isLegalQueenMove();
        assertTrue(value);
    }

    @Test
    void testIsLegalRookMove() {
        boolean value= legalmove.isLegalRookMove();
        assertTrue(value);
    }

    @Test
    void testIsLegalBishopMove() {
        boolean value= legalmove.isLegalBishopMove();
        assertTrue(value);
    }

    @Test
    void testCheckingSides() {
        boolean value= legalmove.checkingSides(b,move,currentSquare);
        assertTrue(value);
    }

    @Test
    void testCheckSameFile() {
        boolean value= legalmove.checkSameFile(b,move);
        assertTrue(value);
    }

    @Test
    void testCheckSameRank() {
        boolean value= legalmove.checkSameRank(b,move);
        assertTrue(value);
    }

    @Test
    void testCheckSameDiagonal() {
        boolean value= legalmove.checkSameDiagonal(b,move);
        assertTrue(value);
    }
}