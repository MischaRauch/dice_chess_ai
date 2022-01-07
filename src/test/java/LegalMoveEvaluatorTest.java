import logic.LegalMoveEvaluator;
import logic.Move;
import logic.State;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;
import logic.game.HumanGame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LegalMoveEvaluatorTest {

    LegalMoveEvaluator evaluator = new LegalMoveEvaluator();

    // Tests are self-contained, better practice to be able to run them in isolation
    @Test
    @DisplayName("d2Pd4 P Non-capture Double-jump Legal")
    void testIsLegalDoubleJumpMovePawnWhite() {
        Move potentialMove = new Move(Piece.WHITE_PAWN, Square.d2, Square.d4, 1, Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1"), 1, Side.WHITE);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("e7pe5 p Non-capture Double-jump Legal")
    void testIsLegalDoubleJumpMovePawnBlack() {
        Move potentialMove = new Move(Piece.BLACK_PAWN, Square.e7, Square.e5, 1, Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.BLACK);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("e5Pd4 P Basic Capture p Legal")
    void testIsLegalMovePawnWhiteCapture() {
        Move potentialMove = new Move(Piece.WHITE_PAWN, Square.d4, Square.e5, 1, Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 1 1"), 1, Side.WHITE);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("d7pd6 p Non-capture Single-jump Legal")
    void testIsLegalSingleMovePawnBlack() {
        Move potentialMove = new Move(Piece.BLACK_PAWN, Square.d7, Square.d6, 1, Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.BLACK);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("d7pd6 p Non-capture Single-jump Illegal")
    void testIsIllegalSingleMovePawnBlack() {
        Move potentialMove = new Move(Piece.BLACK_PAWN, Square.d7, Square.d6, 1, Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/3P4/8/8/8/PPP1PPPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.BLACK);
        assertFalse(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("d6pe5 p Basic Capture P Legal")
    void testIsLegalMovePawnBlackCapture() {
        Move potentialMove = new Move(Piece.BLACK_PAWN, Square.d6, Square.e5, 1, Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/ppp1pppp/3p4/4P3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.BLACK);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("g1Nf3 N Non-capture Legal")
    void testIsLegalMoveKnightWhite() {
        Move potentialMove = new Move(Piece.WHITE_KNIGHT, Square.g1, Square.f3, 2, Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1"), 2, Side.WHITE);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("f3Ne5 N Capture p Legal")
    void testIsLegalMoveKnightWhiteCapture() {
        Move potentialMove = new Move(Piece.WHITE_KNIGHT, Square.f3, Square.e5, 2, Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/pppppp1p/8/4p3/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 0 1 1"), 2, Side.WHITE);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("c1Bh6 B Capture p Legal")
    void testIsLegalMoveBishopWhite() {
        Move potentialMove = new Move(Piece.WHITE_BISHOP, Square.c1, Square.h6, 3, Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR w KQkq - 0 1 1"), 3, Side.WHITE);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("c8bd7 b Non-capture Legal")
    void testIsLegalMoveBishopBlack() {
        Move potentialMove = new Move(Piece.BLACK_BISHOP, Square.c8, Square.d7, 3, Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/ppp1pppp/3p4/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 3, Side.BLACK);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("d5Nd7 N Capture p Legal 2")
    void testIsLegalMoveKnightWhiteCapture2() {
        Move potentialMove = new Move(Piece.WHITE_KNIGHT, Square.e5, Square.d7, 2, Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/3N4/8/8/PPPPPPPP/R1BQKBNR w KQkq - 0 1 1"), 2, Side.WHITE);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("d8qd7 q Capture P Legal")
    void testIsLegalMoveQueenBlackCapture() {
        Move potentialMove = new Move(Piece.BLACK_QUEEN, Square.d8, Square.d7, 5, Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/pppPpppp/8/8/8/8/PPP1PPPP/RNBQKBNR b KQkq - 0 1 1"), 5, Side.BLACK);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("d1Qd7 Q Capture b Legal")
    void testIsLegalMoveQueenWhiteCapture() {
        Move potentialMove = new Move(Piece.WHITE_QUEEN, Square.d1, Square.d7, 5, Side.WHITE);
        State state = new State(new Board0x88("rn1qkbnr/pppbpppp/8/8/8/4P3/PPP1PPPP/RNBQKBNR w KQkq - 0 1 1"), 5, Side.WHITE);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("b8nd7 n Capture P Legal ")
    void testIsLegalMoveKnightBlackCapture() {
        Move potentialMove = new Move(Piece.BLACK_KNIGHT, Square.b8, Square.d7, 2, Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/pppPpppp/8/8/8/8/PPP1PPPP/RNBQKBNR b KQkq - 0 1 1"), 2, Side.BLACK);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("e8kc8 k Non-capture Illegal")
    void testIsIllegalMoveKingBlack() {
        Move potentialMove = new Move(Piece.BLACK_KING, Square.e8, Square.c8, 6, Side.BLACK);
        State state = new State(new Board0x88("rn2kbnr/pbpqpppp/8/1p1p4/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 6, Side.BLACK);
        assertFalse(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("e8kf8 k Non-capture Legal")
    void testIsLegalMoveKingBlackEast() {
        Move potentialMove = new Move(Piece.BLACK_KING, Square.e8, Square.f8, 6, Side.BLACK);
        State state = new State(new Board0x88("rnbqk1nr/ppppppbp/8/6p1/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 6, Side.BLACK);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("e8ke6 k Non-capture Short-castling Legal")
    void testIsLegalMoveKingCastle() {
        Move potentialMove = new Move(Piece.BLACK_KING, Square.e8, Square.g8, 6, Side.BLACK);
        State state = new State(new Board0x88("rnbqk2r/ppppppbp/5n2/6p1/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 6, Side.BLACK);
        assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
    }

    @Test
    @DisplayName("e8ke6 k Non-capture Short-castling Legal r updation State List<PieceAndSquare")
    void testIsLegalMoveKingBlackCastleRookBlackUpdation() {
        Game game = new HumanGame("rnbqk2r/ppppppbp/5n2/6p1/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1");
        Move potentialMove = new Move(Piece.BLACK_KING, Square.e8, Square.g8, 6, Side.BLACK);
        State state = new State(new Board0x88("rnbqk2r/ppppppbp/5n2/6p1/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 6, Side.BLACK);
        //assertTrue(evaluator.isLegalMove(potentialMove, state, true, true));
        state.applyMove(potentialMove);
        assertTrue(state.getPieceAndSquare().get(state.getPieceAndSquare().size()-2).getPiece()==Piece.BLACK_ROOK);
    }

}