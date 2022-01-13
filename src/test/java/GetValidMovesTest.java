import logic.Move;
import logic.State;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.AiAiGame;
import logic.game.Game;
import logic.player.AIPlayer;
import logic.player.BasicAIPlayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    @DisplayName("Short Castling for King White")
    void testValidMovesShortCastlingWhiteKing() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("r1bqk2r/pp1pppbp/n1p2n2/6p1/6P1/5N2/PPPPPPBP/RNBQK2R w KQkq - 0 1"), 6, Side.WHITE);
        assertTrue(basicAI.getValidMoves(state).size() == 2);
    }

    @Test
    @DisplayName("Short Castling for King Black")
    void testValidMovesShortCastlingBlackKing() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("r1bqk2r/pp1pppbp/n1p2n2/6p1/6P1/5N2/PPPPPPBP/RNBQK2R w KQkq - 0 1"), 6, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 2);
    }

    @Test
    @DisplayName("Long Castling for King White")
    void testValidMovesLongCastlingWhiteKing() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("r3kbnr/pbqppppp/n1p5/1p6/1P6/N1P5/PBQPPPPP/R3KBNR w KQkq - 0 1"), 6, Side.WHITE);
        assertTrue(basicAI.getValidMoves(state).size() == 2);
    }

    @Test
    @DisplayName("Long Castling for King Black")
    void testValidMovesLongCastlingBlackKing() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("r3kbnr/pbqppppp/n1p5/1p6/1P6/N1P5/PBQPPPPP/R3KBNR w KQkq - 0 1"), 6, Side.BLACK);
        assertTrue(basicAI.getValidMoves(state).size() == 2);
    }

    @Test
    @DisplayName("En Passant settings for Pawn White")
    void testValidMovesSetEnPassantPawnWhite() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        AIPlayer basicAIB = new BasicAIPlayer(Side.BLACK);
        Game game = new AiAiGame(basicAI, basicAIB, "rnbqkbnr/ppp1pppp/8/8/3p4/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        State state = new State(new Board0x88("rnbqkbnr/ppp1pppp/8/8/3p4/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), 1, Side.WHITE);
        List<Move> validMoves = basicAI.getValidMoves(state);
        state = state.applyMove(validMoves.get(1));
        assertTrue(state.getEnPassant() == Square.a3);
    }

    @Test
    @DisplayName("En Passant settings for Pawn Black")
    void testValidMovesSetEnPassantPawnBlack() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        AIPlayer basicAIB = new BasicAIPlayer(Side.BLACK);
        Game game = new AiAiGame(basicAI, basicAIB, "rnbqkbnr/ppp1pppp/8/8/3p4/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        State state = new State(new Board0x88("rnbqkbnr/ppp1pppp/8/8/3p4/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), 1, Side.WHITE);
        List<Move> validMoves = basicAIB.getValidMoves(state);
        state = state.applyMove(validMoves.get(1));
        assertTrue(state.getEnPassant() == Square.a6);
    }

    @Test
    @DisplayName("En Passant for Pawn White")
    void testValidMovesEnPassantPawnWhite() {
        AIPlayer basicAI = new BasicAIPlayer(Side.WHITE);
        State state = new State(new Board0x88("rnbqkbnr/ppp1pppp/8/2Pp4/8/8/PP1PPPPP/RNBQKBNR b KQkq - 0 1"), 1, Side.WHITE);
        state.setEnPassant(Square.d6);
        assertTrue(basicAI.getValidMoves(state).size() == 16);
    }

    @Test
    @DisplayName("En Passant for Pawn Black")
    void testValidMovesEnPassantPawnBlack() {
        AIPlayer basicAI = new BasicAIPlayer(Side.BLACK);
        State state = new State(new Board0x88("rnbqkbnr/pp1ppppp/8/8/2pP4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 1"), 1, Side.BLACK);
        state.setEnPassant(Square.d3);
        assertTrue(basicAI.getValidMoves(state).size() == 16);
    }

    @Test
    @DisplayName("Promotion for Pawn White")
    void testValidMovesPromotionPawnWhite() {
        AIPlayer basicAIW = new BasicAIPlayer(Side.WHITE);
        AIPlayer basicAIB = new BasicAIPlayer(Side.BLACK);
        AiAiGame game = new AiAiGame(basicAIW, basicAIB, "3qk1nr/1P1ppppp/2n5/8/3P4/2P2P2/P3P1PP/RNBKQBNR w - - 0 1");
        State state = new State(new Board0x88("3qk1nr/1P1ppppp/2n5/8/3P4/2P2P2/P3P1PP/RNBKQBNR w - - 0 1"), 1, Side.WHITE);
        List<Move> allPossibleMoves = basicAIW.getValidMoves(state);
        Move promotionMove = null;
        for (Move move : allPossibleMoves)
            if (move.getDestination() == Square.b8)
                promotionMove = move;
        state = state.applyMove(promotionMove);
        assertTrue(state.getBoard().getPieceAt(Square.b8) == Piece.WHITE_QUEEN);
    }

    @Test
    @DisplayName("Promotion for Pawn Black")
    void testValidMovesPromotionPawnBlack() {
        AIPlayer basicAIW = new BasicAIPlayer(Side.WHITE);
        AIPlayer basicAIB = new BasicAIPlayer(Side.BLACK);
        AiAiGame game = new AiAiGame(basicAIW, basicAIB, "rnpqkpnr/p1pppp2/1Pn5/7P/3P4/2P2P2/P3P1p1/RNBKQ2R b - - 0 1");
        State state = new State(new Board0x88("rnpqkpnr/p1pppp2/1Pn5/7P/3P4/2P2P2/P3P1p1/RNBKQ2R b - - 0 1"), 1, Side.BLACK);
        List<Move> allPossibleMoves = basicAIB.getValidMoves(state);
        Move promotionMove = null;
        for (Move move : allPossibleMoves)
            if (move.getDestination() == Square.h1)
                promotionMove = move;
        state = state.applyMove(promotionMove);
        assertTrue(state.getBoard().getPieceAt(Square.h1) == Piece.BLACK_QUEEN);
    }

}