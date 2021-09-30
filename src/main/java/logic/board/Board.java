package logic.board;

import logic.LegalMoveEvaluator;
import logic.Move;
import logic.State;
import logic.enums.Side;
import logic.enums.Piece;
import logic.enums.Square;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;

public abstract class Board {

    public static void main(String[] args) {
        //for testing purposes
//        Board b = new Board0x88(Board0x88.openingFEN);
//        b.printBoard();
//
//        Board afterVariousMoves = b.movePiece(Square.c2, Square.c3)
//                .movePiece(Square.b8, Square.c6)
//                .movePiece(Square.b2, Square.b4)
//                .movePiece(Square.c6, Square.b4);
//
//        System.out.println(afterVariousMoves.isFriendly(afterVariousMoves.getPieceAt(Square.a1), Side.BLACK));
//        System.out.println(afterVariousMoves.getPieceAt(Square.f7).isFriendly(Side.BLACK));
//
//        System.out.println(afterVariousMoves.isEmpty(Square.b2));
//
//        Move move = new Move(Piece.WHITE_PAWN, Square.c3, Square.b4, 1, Side.WHITE);
//        afterVariousMoves.movePiece(move.getOrigin(), move.getDestination());
//
//        System.out.println("Square above A1: " + afterVariousMoves.getSquareAbove(Square.a1));
//        System.out.println("Piece on square above A1: " + afterVariousMoves.getPieceAt(Square.a1.getSquareAbove()));
//        System.out.println("Square number of square above A1: " + Square.a1.getSquareAbove().getSquareNumber());
//        System.out.println("Board index of square above A1: " + Square.a1.getSquareAbove().getBoardIndex());
//
//        System.out.println("Get square (3, 4) by coordinate (0-indexed): " + Square.getSquare(3, 4));
//        System.out.println("Piece at square (7, 3) by coordinate (0-indexed): " + afterVariousMoves.getPieceAt(Square.getSquare(7, 3)));
//
//        Board c = new Board0x88();
//        c.printBoard();
//
//        System.out.println("Rank: " + Square.a2.getRank() + " file: " + Square.a2.getFile());
//        System.out.println("diff: " + Square.a2.getDiff(Square.a1));
//        System.out.println("file: " + b.getFile(Square.c3));

        LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
        Board testLegalMove = new Board0x88(Board0x88.openingFEN);
        testLegalMove.printBoard();
        Move potentialMove = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4,1, Side.WHITE);
        State state = new State(testLegalMove, 1,Side.WHITE);
        //System.out.println("is valid move: " + evaluator.isLegalMove(potentialMove, state));
        if (evaluator.isLegalMove(potentialMove, state)) {
            System.out.println("VALID MOVE");
            state = state.applyMove(potentialMove);
        } else System.out.println("INVALID MOVE");

        Move potentialMove2 = new Move(Piece.WHITE_PAWN, Square.b2, Square.b3,1, Side.WHITE);

        if (evaluator.isLegalMove(potentialMove2, state)) {
            System.out.println("VALID MOVE");
            state = state.applyMove(potentialMove2);
        } else System.out.println("INVALID MOVE");

        Move potentialMove3 = new Move(Piece.WHITE_KING, Square.d4, Square.d4,1, Side.WHITE);

        if (evaluator.isLegalMove(potentialMove3, state)) {
            System.out.println("VALID MOVE King");
            state = state.applyMove(potentialMove3);
        } else System.out.println("INVALID MOVE King");
    }


    public abstract boolean isEmpty(Square square);

    //this is kinda only relevant for 0x88 i think
    public abstract boolean isOffBoard(int squareNumber);

    public abstract Piece getPieceAt(Square square);

    public abstract Board movePiece(Square origin, Square destination);

    //not sure if this belongs in Square enum or here. Right now it's in both places
    public Square getSquareAbove(Square square) {
        return Square.getSquare(square.getSquareNumber() + 16);
    }

    //not sure if this belongs in Square enum or here. Right now it's in both places
    public Square getSquareBelow(Square square) {
        return Square.getSquare(square.getSquareNumber() - 16);
    }

    public Square[] getRank(int rank) {
        return new Square[0];
    }

    public EnumSet<Square> getFile(Square square) {
        EnumSet<Square> file = EnumSet.noneOf(Square.class);
        //Square[] file = new Square[8];
        for (int i = 0; i < 8; i++) {
            file.add(Square.getSquare(i, square.getFile()));
            //file[i] = Square.getSquare(i, square.getFile());
        }
        return file;
    }

    public Square[] getDiagonals(Square square) {
        return new Square[0];
    }

    public boolean isFriendly(Piece p, Piece o) {
        return p.getColor() == o.getColor();
    }

    public boolean isFriendly(Piece piece, Side side) {
        return piece.isFriendly(side);
    }

    public abstract Board loadFromFEN(String FEN);

    public abstract void printBoard();


}
