import logic.Config;
import logic.LegalMoveEvaluator;
import logic.Move;
import logic.State;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LegalMoveEvaluatorTest {

    LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    Board startBoard = new Board0x88(Config.OPENING_FEN);
    Move potentialMove;
    State state;
    public static State tempState;

    @Test
    @Order(1)
    @DisplayName("1")
    void testIsLegalMovePawnWhite() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.d2, Square.d4,1, Side.WHITE);
        state = new State(startBoard, 1,Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(2)
    @DisplayName("2")
    void testIsLegalMovePawnBlack() {
        potentialMove = new Move(Piece.BLACK_PAWN, Square.e7, Square.e5,1, Side.BLACK);
        state = new State(tempState.board, 1,Side.BLACK);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(3)
    @DisplayName("3")
    void testIsLegalMovePawnWhiteCapture() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.d4, Square.e5, 1, Side.WHITE);
        state = new State(tempState.board, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);

        assertTrue(value);
    }

    @Test
    @Order(4)
    @DisplayName("4")
    void testIsLegalMovePawnBlack2() {
        potentialMove = new Move(Piece.BLACK_PAWN, Square.d7, Square.d6, 1, Side.BLACK);
        state = new State(tempState.board, 1, Side.BLACK);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(5)
    @DisplayName("5")
    void testIsLegalMovePawnBlackCapture() {
        potentialMove = new Move(Piece.BLACK_PAWN, Square.d6, Square.e5, 1, Side.BLACK);
        state = new State(tempState.board, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(6)
    @DisplayName("6")
    void testIsLegalMoveKnightWhite() {
        potentialMove = new Move(Piece.WHITE_KNIGHT, Square.g1, Square.f3, 1, Side.WHITE);
        state = new State(tempState.board, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(7)
    @DisplayName("7")
    void testIsLegalMoveKnightWhiteCapture() {
        potentialMove = new Move(Piece.WHITE_KNIGHT, Square.f3, Square.e5, 1, Side.WHITE);
        state = new State(tempState.board, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(8)
    @DisplayName("8")
    void testIsLegalMoveBishopWhite() {
        potentialMove = new Move(Piece.WHITE_BISHOP, Square.c1, Square.h6, 1, Side.WHITE);
        state = new State(tempState.board, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(9)
    @DisplayName("9")
    void testIsLegalMoveBishopBlack() {
        potentialMove = new Move(Piece.BLACK_BISHOP, Square.c8, Square.d7, 1, Side.BLACK);
        state = new State(tempState.board, 1, Side.BLACK);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(10)
    @DisplayName("10")
    void testIsLegalMoveKnightWhiteCapture2() {
        potentialMove = new Move(Piece.WHITE_KNIGHT, Square.e5, Square.d7, 1, Side.WHITE);
        state = new State(tempState.board, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(11)
    @DisplayName("11")
    void testIsLegalMoveQueenBlackCapture() {
        potentialMove = new Move(Piece.BLACK_QUEEN, Square.d8, Square.d7, 1, Side.BLACK);
        state = new State(tempState.board, 1, Side.BLACK);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(12)
    @DisplayName("12")
    void testIsLegalMoveQueenWhiteCapture() {
        potentialMove = new Move(Piece.WHITE_QUEEN, Square.d1, Square.d7, 1, Side.WHITE);
        state = new State(tempState.board, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(13)
    @DisplayName("13")
    void testIsLegalMoveKnightBlackCapture() {
        potentialMove = new Move(Piece.BLACK_KNIGHT, Square.b8, Square.d7, 1, Side.BLACK);
        state = new State(tempState.board, 1, Side.BLACK);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(14)
    @DisplayName("14")
    void testIsLegalMoveRookBlack() {
        potentialMove = new Move(Piece.BLACK_KING, Square.e8, Square.c8, 1, Side.BLACK);
        state = new State(tempState.board, 1, Side.BLACK);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        tempState = state.applyMove(potentialMove);
        assertTrue(value);
    }

    @Test
    @Order(15)
    @DisplayName("15")
    void testIsLegalMoveKingCastle() {
        potentialMove = new Move(Piece.BLACK_KING, Square.e8, Square.e6, 1, Side.BLACK);
        state = new State(tempState.board, 1, Side.BLACK);
        boolean value = evaluator.isLegalMove(potentialMove,state, true,true);
        assertFalse(value);
    }

    @Test
    void testIsLegalQueenMove() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
        state = new State(startBoard, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove, state, true, true);
        assertTrue(value);
    }

    @Test
    void testIsLegalRookMove() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
        state = new State(startBoard, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove, state, true, true);
        assertTrue(value);
    }

    @Test
    void testIsLegalBishopMove() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
        state = new State(startBoard, 1, Side.WHITE);
        boolean value = evaluator.isLegalMove(potentialMove, state, true, true);
        assertTrue(value);
    }

    @Test
    void testCheckSameFile() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
        boolean value = evaluator.checkSameFile(startBoard, potentialMove);
        assertTrue(value);
    }

    @Test
    void testCheckSameRank() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
        boolean value = evaluator.checkSameRank(startBoard, potentialMove);
        assertTrue(value);
    }

    @Test
    void testCheckSameDiagonal() {
        potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
        boolean value = evaluator.checkSameDiagonal(startBoard, potentialMove);
        assertTrue(value);
    }
}