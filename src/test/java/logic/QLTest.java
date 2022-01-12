package logic;

import logic.ML.DQL;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class QLTest {
        LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
        Board startBoard = new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1");
        State state;

        @Test
        @Order(1)
        void testHowMuchTimeTakes() {
            long startTime = System.currentTimeMillis();
            state = new State(startBoard, 1, Side.WHITE);
            DQL ql = new DQL();
            ql.algo(state, state.getColor(), 2);
            long endTime = System.currentTimeMillis();
            System.out.println("That took " + (endTime - startTime) + " milliseconds");
        }
//
//        @Test
//        @Order(2)
//        void testIsLegalMovePawnBlack() {
//            potentialMove = new Move(Piece.BLACK_PAWN, Square.e7, Square.e5, 1, Side.BLACK);
//            state = new State(tempState.board, 1, Side.BLACK);
//
//            boolean value = evaluator.isLegalMove(potentialMove, state, true);
//            tempState = state.applyMove(potentialMove);
//            assertTrue(value);
//        }
    }

