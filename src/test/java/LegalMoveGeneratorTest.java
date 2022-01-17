import logic.LegalMoveGenerator;
import logic.State;
import logic.board.Board0x88;
import logic.enums.Side;
import logic.enums.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static logic.enums.Piece.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LegalMoveGeneratorTest {

    LegalMoveGenerator generator = new LegalMoveGenerator();

    @Test
    @DisplayName("Legal Moves Pawn White")
    void testLegalMovesPawnWhite() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.d4, WHITE_PAWN);
        assertTrue(validMoves.size() == 3);
    }

    @Test
    @DisplayName("Legal Moves Pawn Black")
    void testLegalMovesPawnBlack() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.e5, BLACK_PAWN);
        assertTrue(validMoves.size() == 2);
    }

    @Test
    @DisplayName("Legal Moves Rook White")
    void testLegalMovesRookWhite() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.a1, WHITE_ROOK);
        assertTrue(validMoves.size() == 3);
    }

    @Test
    @DisplayName("Legal Moves Rook Black")
    void testLegalMovesRookBlack() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.h8, BLACK_ROOK);
        assertTrue(validMoves.size() == 2);
    }

    @Test
    @DisplayName("Legal Moves Knight White")
    void testLegalMovesKnightWhite() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.f3, WHITE_KNIGHT);
        assertTrue(validMoves.size() == 5);
    }

    @Test
    @DisplayName("Legal Moves Rook Black")
    void testLegalMovesKnightBlack() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.f6, BLACK_KNIGHT);
        assertTrue(validMoves.size() == 5);
    }

    @Test
    @DisplayName("Legal Moves Bishop White")
    void testLegalMovesBishopWhite() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.h3, WHITE_BISHOP);
        assertTrue(validMoves.size() == 6);
    }

    @Test
    @DisplayName("Legal Moves Bishop Black")
    void testLegalMovesBishopBlack() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.c5, BLACK_BISHOP);
        assertTrue(validMoves.size() == 5);
    }

    @Test
    @DisplayName("Legal Moves Queen White")
    void testLegalMovesQueenWhite() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.d3, WHITE_QUEEN);
        assertTrue(validMoves.size() == 13);
    }

    @Test
    @DisplayName("Legal Moves Queen Black")
    void testLegalMovesQueenBlack() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.e7, BLACK_QUEEN);
        assertTrue(validMoves.size() == 4);
    }

    @Test
    @DisplayName("Legal Moves King White")
    void testLegalMovesKingWhite() {
        State state = new State(new Board0x88("rnb1k1r1/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K1R1 w Qq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.e1, WHITE_KING);
        assertTrue(validMoves.size() == 3);
    }

    @Test
    @DisplayName("Legal Moves King Black")
    void testLegalMovesKingBlack() {
        State state = new State(new Board0x88("rnb1k1r1/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K1R1 w Qq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.e8, BLACK_KING);
        assertTrue(validMoves.size() == 2);
    }

    @Test
    @DisplayName("Legal Moves King White with short Castling")
    void testLegalMovesKingWhiteShortCastling() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.e1, WHITE_KING);
        assertTrue(validMoves.size() == 4);
    }

    @Test
    @DisplayName("Legal Moves King Black with short Castling")
    void testLegalMovesKingBlackShortCastling() {
        State state = new State(new Board0x88("rnb1k2r/p1ppqppp/5n2/2b1p3/p2P4/3Q1NPB/1PP1PP1P/RNB1K2R w KQkq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.e8, BLACK_KING);
        assertTrue(validMoves.size() == 3);
    }

    @Test
    @DisplayName("Legal Moves King White with long Castling")
    void testLegalMovesKingWhiteLongCastling() {
        State state = new State(new Board0x88("r3kQr1/pbppq1p1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 b Qq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.e1, WHITE_KING);
        assertTrue(validMoves.size() == 3);
    }

    @Test
    @DisplayName("Legal Moves King Black with long Castling")
    void testLegalMovesKingBlackLongCastling() {
        State state = new State(new Board0x88("r3kQr1/pbppq1p1/2n2n2/2b1p2p/pP1P4/2NQ1N1B/2PBPP1P/R3K1R1 b Qq - 0 1"), 1, Side.WHITE);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.e8, BLACK_KING);
        assertTrue(validMoves.size() == 4);
    }

    @Test
    @DisplayName("Legal Moves Pawn White with EnPassant")
    void testLegalMovesPawnWhiteEnPassant() {
        State state = new State(new Board0x88("rnb1k1r1/p1ppqpp1/5n2/2b1p1Pp/pP1P4/3Q1N1B/2P1PP1P/RNB1K1R1 w Qq h6 0 1"), 1, Side.WHITE);
        state.setEnPassant(Square.h6);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.g5, WHITE_PAWN);
        assertTrue(validMoves.size() == 3);
    }

    @Test
    @DisplayName("Legal Moves Pawn Black with EnPassant")
    void testLegalMovesPawnBlackEnPassant() {
        State state = new State(new Board0x88("rnb1k1r1/p1ppqppp/5n2/2b1p3/pP1P4/3Q1NPB/2P1PP1P/RNB1K1R1 b Qq b3 0 1"), 1, Side.WHITE);
        state.setEnPassant(Square.b3);
        List<Square> validMoves = LegalMoveGenerator.getMoves(state, Square.a4, BLACK_PAWN);
        assertTrue(validMoves.size() == 2);
    }
}
