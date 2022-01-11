import logic.State;
import logic.board.Board0x88;
import logic.enums.Side;
import logic.player.AIPlayer;
import logic.player.BasicAIPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetValidMovesTest {

    @Test
    @DisplayName("Starting Moves for Pawns White")
    void testValidMovesStartWhitePawn() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.WHITE);
        assertTrue(basicAI.getValidMoves(state).size() == 16);
    }

    @Test
    @DisplayName("Starting Moves for Pawns Black")
    void testValidMovesStartBlackPawn() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 16);
    }

    @Test
    @DisplayName("Starting Moves for Knights White")
    void testValidMovesStartWhiteKnight() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 2, Side.WHITE);
        assertTrue(basicAI.getValidMoves(state).size() == 4);
    }

    @Test
    @DisplayName("Starting Moves for Pawns Black")
    void testValidMovesStartBlackKnight() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 2, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 4);
    }

    @Test
    @DisplayName("Diagonal Moves for Bishop White")
    void testValidMovesDiagonalWhiteBishop() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("B7/8/8/8/8/8/8/8 b KQkq - 0 1 1"), 3, Side.WHITE);
        System.out.println(basicAI.getValidMoves(state));
        assertTrue(basicAI.getValidMoves(state).size() == 7);
    }

    @Test
    @DisplayName("Diagonal Moves for Bishop Black")
    void testValidMovesDiagonalBlackBishop() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("b7/8/8/8/8/8/8/8 b KQkq - 0 1 1"), 3, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 7);
    }

    @Test
    @DisplayName("Middle Moves for Bishop White")
    void testValidMovesMiddleWhiteBishop() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("8/8/8/3B4/8/8/8/8 b KQkq - 0 1 1"), 3, Side.WHITE);
        System.out.println(basicAI.getValidMoves(state));
        assertTrue(basicAI.getValidMoves(state).size() == 13);
    }

    @Test
    @DisplayName("Middle Moves for Bishop Black")
    void testValidMovesMiddleBlackBishop() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("8/8/8/3b4/8/8/8/8 b KQkq - 0 1 1"), 3, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 13);
    }

    @Test
    @DisplayName("Start Moves for Rook White")
    void testValidMovesStartWhiteRook() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("P7/8/8/8/8/8/8/RN6 b KQkq - 0 1 1"), 4, Side.WHITE);
        System.out.println(basicAI.getValidMoves(state));
        assertTrue(basicAI.getValidMoves(state).size() == 6);
    }

    @Test
    @DisplayName("Start Moves for Rook Black")
    void testValidMovesStartBlackRook() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("rn6/8/8/8/8/8/8/8 b KQkq - 0 1 1"), 4, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 7);
    }

    @Test
    @DisplayName("Middle Moves for Rook White")
    void testValidMovesMiddleWhiteRook() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("8/8/8/3R4/8/8/8/8 b KQkq - 0 1 1"), 4, Side.WHITE);
        System.out.println(basicAI.getValidMoves(state));
        assertTrue(basicAI.getValidMoves(state).size() == 14);
    }

    @Test
    @DisplayName("Middle Moves for Rook Black")
    void testValidMovesMiddleBlackRook() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("8/8/8/3r4/8/8/8/8 b KQkq - 0 1 1"), 4, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 14);
    }

    @Test
    @DisplayName("Start Moves for Queen White")
    void testValidMovesStartWhiteQueen() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("8/8/8/8/8/8/8/3Q4 b KQkq - 0 1 1"), 5, Side.WHITE);
        System.out.println(basicAI.getValidMoves(state));
        assertTrue(basicAI.getValidMoves(state).size() == 21);
    }

    @Test
    @DisplayName("Start Moves for Queen Black")
    void testValidMovesStartBlackQueen() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/8/8/8/3P4/8/8/8 b KQkq - 0 1 1"), 5, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 11);
    }

    @Test
    @DisplayName("Middle Moves for Queen White")
    void testValidMovesMiddleWhiteQueen() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("8/8/8/2ppp3/3Q4/2p5/8/8 b KQkq - 0 1 1"), 5, Side.WHITE);
        assertTrue(basicAI.getValidMoves(state).size() == 17);
    }

    @Test
    @DisplayName("Middle Moves for Queen Black")
    void testValidMovesMiddleBlackQueen() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("8/8/8/2PPP3/3q4/2P5/8/8 b KQkq - 0 1 1"), 5, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 17);
    }

    @Test
    @DisplayName("Start Moves for King White")
    void testValidMovesStartWhiteKing() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("8/8/8/8/8/8/8/3K4 b KQkq - 0 1 1"), 6, Side.WHITE);
        assertTrue(basicAI.getValidMoves(state).size() == 5);
    }

    @Test
    @DisplayName("Start Moves for King Black")
    void testValidMovesStartBlackKing() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("3k4/8/8/8/8/8/8/8 b KQkq - 0 1 1"), 6, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 5);
    }

    @Test
    @DisplayName("Start Moves for King White")
    void testValidMovesMiddleWhiteKing() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("8/8/2P5/3K4/4P3/8/8/8 b KQkq - 0 1 1"), 6, Side.WHITE);
        assertTrue(basicAI.getValidMoves(state).size() == 6);
    }

    @Test
    @DisplayName("Middle Moves for King Black")
    void testValidMovesMiddleBlackKing() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("8/8/2p5/3k4/4P3/8/8/8 b KQkq - 0 1 1"), 6, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 7);
    }

}