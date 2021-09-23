package logic.board;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Board0x88 extends Board {

    static Map<Integer, Integer> boardIndexMap = new HashMap<>();

    static String tricky = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 1";
    static String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";

    static {
        //populate the boardIndexMap
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardIndexMap.put(i * 16 + j, (112 + j) - i * 16);
            }
        }
    }

    Piece[] board;

    public Board0x88() {
        board = new Piece[128];
        for (int i = 0x00; i < board.length; i++) {
            if ((i & 0x88) == 0)
                board[i] = Piece.EMPTY;
            else
                board[i] = Piece.OFF_BOARD;
        }
    }

    public Board0x88(String FEN) {
        this();
        loadFromFEN(FEN);
    }

    public Board0x88(Piece[] board) {
        this.board = Arrays.copyOf(board, board.length);
    }

    @Override
    public Board loadFromFEN(String FEN) {
        //This splits the FEN String into an array of Strings; the first 8 (0-7) indices contain
        //the FEN encoding of that rank. The rest of the indices are not used for the board

        String[] info = FEN.split("/|\\s"); //either split on "/" or on " " (whitespace)

        for (int rank = 0; rank < 8; rank++) {
            //turn the FEN rank String into a char array for processing
            char[] rankSequence = info[rank].toCharArray();
            int file = 0;

            for (char c : rankSequence) {
                if (Character.isDigit(c)) {
                    file += Character.getNumericValue(c);
                } else {
                    int square = rank * 16 + file;
                    board[square] = Piece.getPieceFromChar(c);
                    file++;
                }
            }
        }

        return this;
    }

    @Override
    public boolean isEmpty(Square square) {
        return getPieceAt(square) == Piece.EMPTY;
    }

    @Override
    public boolean isOffBoard(int squareNumber) {
        return (squareNumber & 0x88) != 0;
    }

    @Override
    public Piece getPieceAt(Square square) {
        return board[square.getBoardIndex()];
    }

    @Override
    public Board movePiece(Square origin, Square destination) {
        Board0x88 boardAfterMove = new Board0x88(board);

        boardAfterMove.board[destination.getBoardIndex()] = boardAfterMove.board[origin.getBoardIndex()];
        boardAfterMove.board[origin.getBoardIndex()] = Piece.EMPTY;

        boardAfterMove.printBoard(false);

        return boardAfterMove;
    }

    public int getSquareNumber(String square) {
        int file = ((int) square.charAt(0)) - 97; //char value of 'a' is 97
        int rank = Character.getNumericValue(square.charAt(1)) - 1;
        return getSquareNumber(rank, file);
    }

    public int getSquareNumber(int rank, int file) {
        return rank * 16 + file;
    }

    @Override
    public void printBoard(boolean full) {
        final int MAX_FILE = full ? 16 : 8;
        String files = "　　　A　B　C　D　E　F　G　H  \n";
        System.out.println(files);

        for (int rank = 0; rank < 8; rank++) {
            System.out.print(" " + (8 - rank) + " 　");
            for (int file = 0; file < MAX_FILE; file++) {
                int tile = rank * 16 + file; // this converts to board index
                System.out.print(board[tile].getType() + " ");
            }
            System.out.println("　"+ (8 - rank) + " ");
        }
        System.out.println("\n"+files + "\n\n");
    }
}
