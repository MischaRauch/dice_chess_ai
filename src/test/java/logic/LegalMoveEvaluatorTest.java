package logic;
import logic.board.*;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegalMoveEvaluatorTest {
    LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    Board testLegalMoves = new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1");
    Move potentialMove;
    State state;

    @Test
    void testIsLegalMove() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4,1, Side.WHITE);
        state = new State(testLegalMoves, 1,Side.WHITE);

        boolean value= evaluator.isLegalMove(potentialMove,state);
        assertTrue(value);
    }

//    @Test
//    void testIsLegalPawnMove() {
//        Boolean value= legalmove.isLegalPawnMove();
//        assertTrue(value);
//    }
//
//    @Test
//    void testIsLegalKnightMove() {
//        Boolean value= legalmove.isLegalKnightMove();
//        assertTrue(value);
//    }
//
//    @Test
//    void testIsLegalQueenMove() {
//        Boolean value= legalmove.isLegalQueenMove();
//        assertTrue(value);
//    }
//
//    @Test
//    void testIsLegalRookMove() {
//        Boolean value= legalmove.isLegalRookMove();
//        assertTrue(value);
//    }
//
//    @Test
//    void testIsLegalBishopMove() {
//        Boolean value= legalmove.isLegalBishopMove();
//        assertTrue(value);
//    }
//
//    @Test
//    void testCheckingSides() {
//        Boolean value= legalmove.checkingSides(b,move,currentSquare);
//        assertTrue(value);
//    }
//
//    @Test
//    void testCheckSameFile() {
//        Boolean value= legalmove.checkSameFile(b,move);
//        assertTrue(value);
//    }
//
//    @Test
//    void testCheckSameRank() {
//        Boolean value= legalmove.checkSameRank(b,move);
//        assertTrue(value);
//    }
//
//    @Test
//    void testCheckSameDiagonal() {
//        Boolean value= legalmove.checkSameDiagonal(b,move);
//        assertTrue(value);
//    }
}