package logic.player;

import logic.*;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static logic.enums.Piece.EMPTY;
import static logic.enums.Square.INVALID;


public abstract class AIPlayer {

    Side color;

    public AIPlayer(Side color) {
        this.color = color;
    }

    public AIPlayer() {
        this(Side.BLACK);
    }

    public abstract Move chooseMove(State state);
    public abstract String getNameAi();
    //have not tested this
    //need to incorporate castling and promotion
    public List<Move> getValidMoves(State state) {
        List<Move> validMoves = new LinkedList<>();
        Piece piece = Dice.diceToPiece[state.getDiceRoll() - 1].getColoredPiece(color);

        Board board = state.getBoard();
        Board0x88 b = (Board0x88) board;

        //TODO: use piece lists so we don't have to loop through entire logic.board
        for (int i = 0; i < b.getBoardArray().length; i++) {
            Piece p = b.getBoardArray()[i];
            Square location = Square.getSquareByIndex(i);

            if (p == piece) {
                switch (piece.getType()) {
                    case PAWN -> {
                        //this one is more complex and weird since it depends on logic.board state with the en passant and capturing
                        Square naturalMove = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[0]);
                        if (board.isEmpty(naturalMove)) {
                            validMoves.add(new Move(p, location, naturalMove, state.getDiceRoll(), color));

                            //double jumping
                            Square doubleJump = Square.getSquare(naturalMove.getSquareNumber() + piece.getOffsets()[0]);
                            if (doubleJump != Square.INVALID && board.isEmpty(doubleJump) && piece.canDoubleJump(location))
                                validMoves.add(new Move(p, location, doubleJump, state.getDiceRoll(), color));
                        }

                        for (int k = 1; k < 3; k++) {
                            if (!board.isOffBoard(location.getSquareNumber() + piece.getOffsets()[k])) {
                                Square validTarget = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[k]);

                                if (board.getPieceAt(validTarget) != EMPTY && !board.getPieceAt(validTarget).isFriendly(piece.getColor()))
                                    validMoves.add(new Move(p, location, validTarget, state.getDiceRoll(), color));
                            }
                        }
                    }

                    case KNIGHT, KING -> {
                        for (int offset : piece.getOffsets()) {
                            if (!board.isOffBoard(location.getSquareNumber() + offset)) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);

                                if (board.isEmpty(target) || !board.getPieceAt(target).isFriendly(piece.getColor())) {
                                    validMoves.add(new Move(p, location, target, state.getDiceRoll(), color));
                                }
                            }
                        }
                    }

                    case BISHOP, ROOK, QUEEN -> {
                        for (int offset : piece.getOffsets()) {
                            if (!board.isOffBoard(location.getSquareNumber() + offset)) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);

                                while (target != INVALID && board.isEmpty(target)) {
                                    validMoves.add(new Move(p, location, target, state.getDiceRoll(), color));
                                    target = Square.getSquare(target.getSquareNumber() + offset);
                                }

                                if (target != INVALID && !board.getPieceAt(target).isFriendly(piece.getColor())) {
                                    validMoves.add(new Move(p, location, target, state.getDiceRoll(), color));
                                }
                            }
                        }
                    }
                }
            }

        }

        return validMoves;
    }

    public void printPieceAndSquare(List<PieceAndSquareTuple> nodePieceAndSquare) {
        System.out.println("State; pieceAndSquare: Size: " + nodePieceAndSquare.size());
        for (PieceAndSquareTuple t : nodePieceAndSquare) {
            System.out.print(t.toString() + " | ");
        }
        printPieceCounts(nodePieceAndSquare);
    }

    public void printPieceCounts(List<PieceAndSquareTuple> pieceAndSquare) {
        int pawn = 0;
        int knight = 0;
        int rook = 0;
        int bishop = 0;
        int king = 0;
        int queen = 0;
        for (PieceAndSquareTuple t : pieceAndSquare) {
            if (t.getPiece().equals(Piece.BLACK_QUEEN) || t.getPiece().equals(Piece.WHITE_QUEEN)) {
                queen++;
            } else if (t.getPiece().equals(Piece.WHITE_BISHOP) || t.getPiece().equals(Piece.BLACK_BISHOP)) {
                bishop++;
            } else if (t.getPiece().equals(Piece.WHITE_KING) || t.getPiece().equals(Piece.BLACK_KING)) {
                king++;
            } else if (t.getPiece().equals(Piece.WHITE_ROOK) || t.getPiece().equals(Piece.BLACK_ROOK)) {
                rook++;
            } else if (t.getPiece().equals(Piece.WHITE_PAWN) || t.getPiece().equals(Piece.BLACK_PAWN)) {
                pawn++;
            } else if (t.getPiece().equals(Piece.WHITE_KNIGHT) || t.getPiece().equals(Piece.BLACK_KNIGHT)) {
                knight++;
            }
        }
        System.out.println("\nCounts: Pawn: " + pawn + " Knight: " + knight + " Bishop: " + bishop + " Rook: " + rook + " Queen: " + queen + " King: " + king + "\n");
    }
}
