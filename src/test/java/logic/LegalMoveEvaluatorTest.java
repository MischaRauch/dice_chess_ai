package logic;

import logic.board.*;
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

    @Test
   void testIsLegalPawnMove() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4,1, Side.WHITE);
        state = new State(testLegalMoves, 1,Side.WHITE);

        boolean value= evaluator.isLegalPawnMove(potentialMove,state);
        assertTrue(value);
  }

   @Test
    void testIsLegalKnightMove() {
       potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
       state = new State(testLegalMoves, 1, Side.WHITE);

       boolean value = evaluator.isLegalKnightMove(potentialMove, state);
       assertTrue(value);
   }


   @Test
   void testIsLegalQueenMove() {
       potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
       state = new State(testLegalMoves, 1, Side.WHITE);
       boolean value= evaluator.isLegalQueenMove(potentialMove,state);
        assertTrue(value);
   }

    @Test
    void testIsLegalRookMove() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
        state = new State(testLegalMoves, 1, Side.WHITE);
        boolean value= evaluator.isLegalRookMove(potentialMove,state);
        assertTrue(value);
   }

   @Test
   void testIsLegalBishopMove() {
       potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
       state = new State(testLegalMoves, 1, Side.WHITE);
       boolean value= evaluator.isLegalBishopMove(potentialMove,state);
       assertTrue(value);
   }

    @Test
   void testCheckingSides() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);

       boolean value= evaluator.checkingSides(testLegalMoves,potentialMove,Square.c2);
       assertTrue(value);
    }

    @Test
   void testCheckSameFile() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);

        boolean value= evaluator.checkSameFile(testLegalMoves,potentialMove);
       assertTrue(value);
  }

   @Test
  void testCheckSameRank() {
       potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);

       boolean value= evaluator.checkSameRank(testLegalMoves,potentialMove);
       assertTrue(value);
  }

   @Test
   void testCheckSameDiagonal() {
       potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);

       boolean value= evaluator.checkSameDiagonal(testLegalMoves,potentialMove);
       assertTrue(value);
   }
}