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
    String FEN;

    public Board0x88() {
        board = new Piece[128];
    }

    public Board0x88(Piece[] board) {
        this.board = Arrays.copyOf(board, board.length);
    }

    public Board0x88(String FEN) {
        this();
        this.FEN = FEN;

        Arrays.fill(board, Piece.OFF_BOARD);

        //This splits the FEN String into an array of Strings; the first 8 (0-7) indices contain
        //the FEN encoding of that rank. The rest of the indices are not used for the board
        String[] info = FEN.split("/|\\s"); //either split on "/" or on " " (whitespace)

        for (int rank = 0; rank < 8; rank++) {
            //turn the FEN rank String into a char array for processing
            char[] rankSequence = info[rank].toCharArray();
            int file = 0;

            for (char c : rankSequence) {
                if (Character.isDigit(c)) {
                    //digits in the FEN rank sequence represent empty square offsets
                    for (int i = 0; i < Character.getNumericValue(c); i++) {
                        int tile = rank * 16 + (file + i);
                        board[tile] = Piece.EMPTY;
                    }
                    file += Character.getNumericValue(c);
                } else {
                    int tile = rank * 16 + file;
                    board[tile] = Piece.getPieceFromChar(c);
                    file++;
                }
            }
        }
    }

    public int getSquareNumber(String square) {
        int file = ((int) square.charAt(0)) - 97; //char value of 'a' is 97
        int rank = Character.getNumericValue(square.charAt(1)) - 1;
        return rank * 16 + file;
    }

    public int getSquareNumber(int rank, int file) {
        return rank * 16 + file;
    }

    @Override
    public boolean isEmpty(String square) {
        return getPieceAt(square) == Piece.EMPTY;
    }

    @Override
    public boolean isEmpty(Square square) {
        return board[square.getBoardIndex()] == Piece.EMPTY;
    }

    @Override
    public boolean isOffBoard(int squareNumber) {
        return (squareNumber & 0x88) != 0;
    }

    @Override
    public Piece getPieceAt(String square) {
        return board[boardIndexMap.get(getSquareNumber(square))];
    }

    @Override
    public Piece getPieceAt(Square square) {
        return board[square.getBoardIndex()];
    }

    @Override
    public String getSquareAbove(String square) {
        return null;
    }

    @Override
    public String getSquareBelow(String square) {
        return null;
    }

    @Override
    public String[] getRank(int rank) {
        return new String[0];
    }

    @Override
    public String[] getFile(String file) {
        return new String[0];
    }

    @Override
    public String[] getDiagonals(String square) {
        return new String[0];
    }

    @Override
    public boolean isFriendly(String square, Side side) {
        return board[boardIndexMap.get(getSquareNumber(square))].isFriendly(side);
    }

    @Override
    public Board movePiece(String origin, String destination) {
        int originSquare = getSquareNumber(origin);
        int destinationSquare = getSquareNumber(destination);

        Board0x88 result = new Board0x88(board);

        result.board[boardIndexMap.get(destinationSquare)] = result.board[boardIndexMap.get(originSquare)];
        result.board[boardIndexMap.get(originSquare)] = Piece.EMPTY;

        result.printBoard(false);

        return result;
    }

    @Override
    public Board movePiece(Square origin, Square destination) {
        Board0x88 boardAfterMove = new Board0x88(board);

        boardAfterMove.board[destination.getBoardIndex()] = boardAfterMove.board[origin.getBoardIndex()];
        boardAfterMove.board[origin.getBoardIndex()] = Piece.EMPTY;

        boardAfterMove.printBoard(false);

        return boardAfterMove;
    }

    @Override
    public Board loadFromFEN(String FEN) {
        return new Board0x88(FEN);
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
