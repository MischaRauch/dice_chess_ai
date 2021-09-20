package logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board0x88 {

    static Map<Character, Piece> charPieceMap = new HashMap<>();

    public static void main(String[] args) {
        charPieceMap.put('P', Piece.WHITE_PAWN);
        charPieceMap.put('N', Piece.WHITE_KNIGHT);
        charPieceMap.put('B', Piece.WHITE_BISHOP);
        charPieceMap.put('R', Piece.WHITE_ROOK);
        charPieceMap.put('Q', Piece.WHITE_QUEEN);
        charPieceMap.put('K', Piece.WHITE_KING);
        charPieceMap.put('p', Piece.BLACK_PAWN);
        charPieceMap.put('n', Piece.BLACK_KNIGHT);
        charPieceMap.put('b', Piece.BLACK_BISHOP);
        charPieceMap.put('r', Piece.BLACK_ROOK);
        charPieceMap.put('q', Piece.BLACK_QUEEN);
        charPieceMap.put('k', Piece.BLACK_KING);
        charPieceMap.put('o', Piece.OFF_BOARD);

        System.out.println("white chess king    ♔\n" +
                "white chess queen ♕\n" +
                "white chess rook ♖\n" +
                "white chess bishop ♗\n" +
                "white chess knight ♘\n" +
                "white chess pawn ♙\n" +
                "\n" +
                "black chess king ♚\n" +
                "black chess queen ♛\n" +
                "black chess rook ♜\n" +
                "black chess bishop ♝\n" +
                "black chess knight ♞\n" +
                "black chess pawn ♟");

        Board0x88 b = new Board0x88();
    }

//    enum Piece {
//        EMPTY, WHITE_PAWN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_QUEEN, WHITE_KING,
//        BLACK_PAWN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_QUEEN, BLACK_KING, OFF_BOARD
//    }

    enum Piece {
        EMPTY(' '), WHITE_PAWN('P'), WHITE_KNIGHT('N'), WHITE_BISHOP('B'), WHITE_ROOK('R'), WHITE_QUEEN('Q'), WHITE_KING('K'),
        BLACK_PAWN('p'), BLACK_KNIGHT('n'), BLACK_BISHOP('b'), BLACK_ROOK('r'), BLACK_QUEEN('q'), BLACK_KING('k'), OFF_BOARD('o');

        char type;

        Piece(char type) {
            this.type = type;
        }

        public char getType() {
            return type;
        }
    }

    Piece[] board = new Piece[128];

    public Board0x88() {
        createEmptyBoard();
        loadFromFEN(tricky);
        printBoard(false);
    }

    public void createEmptyBoard() {
        Arrays.fill(board, Piece.OFF_BOARD);

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 16; file++) {
                int tile = rank * 16 + file;
                if ((tile & 0x88) == 0) {
                    board[tile] = Piece.EMPTY;
                }
            }
        }
    }

    public void printBoard(boolean full) {
        final int MAX_FILE = full ? 16 : 8;

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < MAX_FILE; file++) {
                int tile = rank * 16 + file; // this converts to board index
                System.out.print(board[tile] + " ");
            }
            System.out.println();
        }
    }

    String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";
    String tricky = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 1";

    public void loadFromFEN(String FEN) {
        String[] info = FEN.split("/|\\s"); //either split on "/" or on " " (whitespace)

        for (int rank = 0; rank < 8; rank++) {
            char[] rankSequence = info[rank].toCharArray();
            int file = 0;
            for (char c : rankSequence) {
                if (Character.isDigit(c)) {
                    file += Character.getNumericValue(c);
                } else {
                    int tile = rank * 16 + file;
                    if ((tile & 0x88) == 0) {
                        board[tile] = charPieceMap.get(c);
                    }
                    file++;
                }
            }
        }

    }


}
