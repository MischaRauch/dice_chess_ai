import logic.State;
import logic.board.Board0x88;
import logic.enums.Side;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static logic.Dice.canMove;
import static logic.enums.Piece.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CanMoveDiceTest {

    @Test
    @DisplayName("P Start")
    void testCanMoveWhitePawn() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.WHITE);
        assertTrue(canMove(WHITE_PAWN, state));
    }

    @Test
    @DisplayName("p Start")
    void testCanMoveBlackPawn() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.BLACK);
        assertTrue(canMove(BLACK_PAWN, state));
    }

    @Test
    @DisplayName("R Start")
    void testCanMoveWhiteRook() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 2, Side.WHITE);
        assertFalse(canMove(WHITE_ROOK, state));
    }

    @Test
    @DisplayName("r Start")
    void testCanMoveBlackRook() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 2, Side.BLACK);
        assertFalse(canMove(BLACK_ROOK, state));
    }

    @Test
    @DisplayName("N Start")
    void testCanMoveWhiteKnight() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 3, Side.WHITE);
        assertTrue(canMove(WHITE_KNIGHT, state));
    }

    @Test
    @DisplayName("n Start")
    void testCanMoveBlackKnight() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 3, Side.BLACK);
        assertTrue(canMove(BLACK_KNIGHT, state));
    }

    @Test
    @DisplayName("B Start")
    void testCanMoveWhiteBishop() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 4, Side.WHITE);
        assertFalse(canMove(WHITE_BISHOP, state));
    }

    @Test
    @DisplayName("b Start")
    void testCanMoveBlackBishop() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 4, Side.BLACK);
        assertFalse(canMove(BLACK_BISHOP, state));
    }

    @Test
    @DisplayName("Q Start")
    void testCanMoveWhiteQueen() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 5, Side.WHITE);
        assertFalse(canMove(WHITE_QUEEN, state));
    }

    @Test
    @DisplayName("q Start")
    void testCanMoveBlackQueen() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 5, Side.BLACK);
        assertFalse(canMove(BLACK_QUEEN, state));
    }

    @Test
    @DisplayName("K Start")
    void testCanMoveWhiteKing() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 6, Side.WHITE);
        assertFalse(canMove(WHITE_KING, state));
    }

    @Test
    @DisplayName("k Start")
    void testCanMoveBlackKing() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 6, Side.BLACK);
        assertFalse(canMove(BLACK_KING, state));
    }

    @Test
    @DisplayName("P capture move")
    void testCanMoveWhitePawnCapture() {
        State state = new State(new Board0x88("8/8/8/8/ppp5/1P6/8/8 b KQkq - 0 1 1"), 1, Side.WHITE);
        assertTrue(canMove(WHITE_PAWN, state));
    }

    @Test
    @DisplayName("p Capture move")
    void testCanMoveBlackPawnCapture() {
        State state = new State(new Board0x88("8/8/1p6/PPP5/8/8/8/8 b KQkq - 0 1 1"), 1, Side.BLACK);
        assertTrue(canMove(BLACK_PAWN, state));
    }

    @Test
    @DisplayName("R capture move")
    void testCanMoveWhiteRookCapture() {
        State state = new State(new Board0x88("8/8/8/8/8/8/p7/RN b KQkq - 0 1 1"), 2, Side.WHITE);
        assertTrue(canMove(WHITE_ROOK, state));
    }

    @Test
    @DisplayName("r Capture move")
    void testCanMoveBlackRookCapture() {
        State state = new State(new Board0x88("rn8/P7/8/8/8/8/8/8 b KQkq - 0 1 1"), 2, Side.BLACK);
        assertTrue(canMove(BLACK_ROOK, state));
    }

    @Test
    @DisplayName("N capture move")
    void testCanMoveWhiteKnightCapture() {
        State state = new State(new Board0x88("8/8/8/8/8/pppppppp/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 3, Side.WHITE);
        assertTrue(canMove(WHITE_KNIGHT, state));
    }

    @Test
    @DisplayName("r Capture move")
    void testCanMoveBlackKnightCapture() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/PPPPPPPP/8/8/8/8/8 b KQkq - 0 1 1"), 3, Side.BLACK);
        assertTrue(canMove(BLACK_KNIGHT, state));
    }

    @Test
    @DisplayName("B capture move")
    void testCanMoveWhiteBishopCapture() {
        State state = new State(new Board0x88("8/8/8/8/8/8/kn/BQ6 b KQkq - 0 1 1"), 4, Side.WHITE);
        assertTrue(canMove(WHITE_BISHOP, state));
    }

    @Test
    @DisplayName("b Capture move")
    void testCanMoveBlackBishopCapture() {
        State state = new State(new Board0x88("bq6/KN/8/8/8/8/8/8 b KQkq - 0 1 1"), 4, Side.BLACK);
        assertTrue(canMove(BLACK_BISHOP, state));
    }

    @Test
    @DisplayName("Q capture move")
    void testCanMoveWhiteQueenCapture() {
        State state = new State(new Board0x88("8/8/8/8/8/8/Kn/QN b KQkq - 0 1 1"), 5, Side.WHITE);
        assertTrue(canMove(WHITE_QUEEN, state));
    }

    @Test
    @DisplayName("q Capture move")
    void testCanMoveBlackQueenCapture() {
        State state = new State(new Board0x88("qn6/kN6/8/8/8/8/8/8 b KQkq - 0 1 1"), 5, Side.BLACK);
        assertTrue(canMove(BLACK_QUEEN, state));
    }

    @Test
    @DisplayName("K capture move")
    void testCanMoveWhiteKingCapture() {
        State state = new State(new Board0x88("8/8/8/pppppppp/pppKpppp/pppppppp/8/8 b KQkq - 0 1 1"), 6, Side.WHITE);
        assertTrue(canMove(WHITE_KING, state));
    }

    @Test
    @DisplayName("k Capture move")
    void testCanMoveBlackKingCapture() {
        State state = new State(new Board0x88("8/8/8/PPPPPPPP/PPPkPPPP/PPPPPPPP/8/8 b KQkq - 0 1 1"), 6, Side.BLACK);
        assertTrue(canMove(BLACK_KING, state));
    }

}
