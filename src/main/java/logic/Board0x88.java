package logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board0x88 {

    public static void main(String[] args) {
        Board0x88 b = new Board0x88();
        b.createEmptyBoard();
        b.loadFromFEN(openingFEN);
        b.printBoard(false);

        b.movePiece("a1", "h3");
    }


    enum Piece {
        EMPTY('　'), WHITE_PAWN('♟'), WHITE_KNIGHT('♞'), WHITE_BISHOP('♝'), WHITE_ROOK('♜'), WHITE_QUEEN('♛'), WHITE_KING('♚'),
        BLACK_PAWN('♙'), BLACK_KNIGHT('♘'), BLACK_BISHOP('♗'), BLACK_ROOK('♖'), BLACK_QUEEN('♕'), BLACK_KING('♔'), OFF_BOARD('o');

        char type;

        Piece(char type) {
            this.type = type;
        }

        public char getType() {
            return type;
        }
    }

    static Map<Character, Piece> charPieceMap = new HashMap<>();

    static String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";
    static String tricky = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 1";

    Piece[] board = new Piece[128];

    public Board0x88() {
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

    public void movePiece(String origin, String destination) {
        //format like file + rank (e.g.: b6, e4, etc)
        int originFile = ((int) origin.charAt(0)) - 97; //char value of 'a' is 97
        int originRank = Character.getNumericValue(origin.charAt(1)) - 1;
        int originBoardIndex = originRank * 16 + originFile;

        System.out.println(board[originBoardIndex]);
        int destFile = ((int) destination.charAt(0)) - 97;; //char value of 'a' is 97
        int destRank = Character.getNumericValue(destination.charAt(1)) - 1;
        int destBoardIndex = destRank * 16 + destFile;
        System.out.println(board[destBoardIndex]);

        //Piece temp = Piece.EMPTY;

        //temp = board[destBoardIndex];
        board[destBoardIndex] = board[originBoardIndex];
        board[originBoardIndex] = Piece.EMPTY;

        printBoard(false);
    }







    public void printBoard(boolean full) {
        final int MAX_FILE = full ? 16 : 8;
        String files = "　　A　B　C　D　E　F　G　H  ";
        System.out.println(files);
        for (int rank = 0; rank < 8; rank++) {
            System.out.print(" " + (8 - rank) + " ");
            for (int file = 0; file < MAX_FILE; file++) {
                int tile = rank * 16 + file; // this converts to board index
                System.out.print(board[tile].getType() + " ");
            }
            System.out.println((8 - rank) + " ");
        }
        System.out.println(files);
    }



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
