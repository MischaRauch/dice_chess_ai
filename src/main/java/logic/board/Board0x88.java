package logic.board;

import logic.enums.Piece;
import logic.enums.Square;

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

    private final Piece[] board;

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

    //easy cloning of board
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
    public void removePiece(Square location) {
        board[location.getBoardIndex()] = Piece.EMPTY;
    }

    @Override
    public void setPiece(Piece piece, Square location) {
        board[location.getBoardIndex()] = piece;
    }

    @Override
    public Board movePiece(Square origin, Square destination) {
        Board0x88 boardAfterMove = new Board0x88(board);

        boardAfterMove.board[destination.getBoardIndex()] = boardAfterMove.board[origin.getBoardIndex()];
        boardAfterMove.board[origin.getBoardIndex()] = Piece.EMPTY;

        return boardAfterMove;
    }

    @Override
    public void printBoard() {
        String files = "\n　 　　A　B　C　D　E　F　G　H  \n";
        System.out.println(files);

        int rank = 8; //print board from top to bottom (white perspective)
        Piece prev = Piece.OFF_BOARD;

        for (Piece p : board) {
            if (prev == Piece.OFF_BOARD && p != Piece.OFF_BOARD)
                System.out.print("　" + rank + "　 ");

            if (p != Piece.OFF_BOARD)
                System.out.print(p.getUnicode() + " ");

            if (prev != Piece.OFF_BOARD && p == Piece.OFF_BOARD)
                System.out.println("　" + rank-- + " ");

            //update previous piece
            prev = p;
        }

        System.out.println(files + "\n");
    }

}
