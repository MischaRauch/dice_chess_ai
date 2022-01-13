package logic.player;

import logic.Dice;
import logic.Move;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.LinkedList;
import java.util.List;

import static logic.enums.Piece.EMPTY;
import static logic.enums.Square.*;


public abstract class AIPlayer {

    Side color;

    public AIPlayer(Side color) {
        this.color = color;
    }

    public AIPlayer() {
        this(Side.NEUTRAL);
    }

    public abstract Move chooseMove(State state);
    public abstract String getNameAi();

    public abstract long getTimeNeeded();

    //have not tested this
    //need to incorporate castling and promotion
    public List<Move> getValidMoves(State state) {
        List<Move> validMoves = new LinkedList<>();
        Piece piece = Dice.diceToPiece[state.getDiceRoll() - 1].getColoredPiece(color);

        Board board = state.getBoard();
        Board0x88 b = (Board0x88) board;

        //TODO: use piece lists so we don't have to loop through entire board
        //TODO: somewhere we need to detect check (both enemy and ours)
        //TODO detect which moves protect the king/valuable pieces from check
        //TODO: avoid moving into squares which are under attack. Maybe depending on how many available pieces opponent has
        //TODO: add move option to promote pawn despite dice roll -> need way to see if advantageous to do so
        for (int i = 0; i < b.getBoardArray().length; i++) {
            Piece p = b.getBoardArray()[i];
            Square location = Square.getSquareByIndex(i);

            if (p == piece) {
                switch (piece.getType()) {
                    case PAWN -> {
                        //first check the natural move in the "forward" direction
                        Square naturalMove = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[0]);
                        if (naturalMove != Square.INVALID && board.isEmpty(naturalMove)) {
                            Move natural = new Move(p, location, naturalMove, state.getDiceRoll(), color);

                            //promote if possible
                            if (p.canPromote(naturalMove)) {
                                //pawn is moving into promotion rank
                                if (state.getDiceRoll() != 1 && state.getDiceRoll() != 6) {
                                    //if dice roll is not pawn or king, then automatically promote piece
                                    natural.promotionPiece = p.promote(state.getDiceRoll());
                                } else {
                                    //auto promote to Queen
                                    //TODO: potentially give AI the choice to promote to knight in cases where it would lead to a king capture in the next turn
                                    natural.promotionPiece = Piece.QUEEN.getColoredPiece(color);
                                }
                                natural.promotionMove = true;
                            }
                            validMoves.add(natural);

                            //double jumping
                            Square doubleJump = Square.getSquare(naturalMove.getSquareNumber() + piece.getOffsets()[0]);
                            if (doubleJump != Square.INVALID && board.isEmpty(doubleJump) && piece.canDoubleJump(location)) {
                                Move move = new Move(p, location, doubleJump, state.getDiceRoll(), color);
                                //set en-passant stuff
                                move.setEnPassant(naturalMove);
                                move.setEnPassantMove(true);
                                validMoves.add(move);
                            }
                        }

                        //now consider pawn capture moves which put the pawn in a different file
                        for (int k = 1; k < 3; k++) {
                            //loop through the two potential capture targets
                            Square captureTarget = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[k]);

                            //only consider valid squares. Not sure if necessary since it's technically impossible for a pawn to be in the last rank
                            //since it would immediately be promoted, but let's just be safe.
                            if (captureTarget != Square.INVALID) {
                                Piece atTarget = b.getPieceAt(captureTarget);

                                if (atTarget == EMPTY) {
                                    //no piece at target, but en-passant capture still possible
                                    if (state.getEnPassant() != INVALID) {
                                        Move capture = new Move(p, location, captureTarget, state.getDiceRoll(), color);
                                        capture.setEnPassantCapture(true);
                                        validMoves.add(capture);
                                    }
                                } else {
                                    //capture target square is occupied by enemy piece
                                    if (!atTarget.isFriendly(color)) {
                                        Move capture = new Move(p, location, captureTarget, state.getDiceRoll(), color);

                                        //check if capture results in promotion and promote if so
                                        if (p.canPromote(captureTarget)) {
                                            //pawn is moving into promotion rank
                                            if (state.getDiceRoll() != 1 && state.getDiceRoll() != 6) {
                                                //if dice roll is not pawn or king, then automatically promote piece
                                                capture.promotionPiece = p.promote(state.getDiceRoll());
                                            } else {
                                                //auto promote to Queen
                                                //TODO: potentially give AI the choice to promote to knight in cases where it would lead to a king capture in the next turn
                                                capture.promotionPiece = Piece.QUEEN.getColoredPiece(color);
                                            }

                                            capture.promotionMove = true;
                                        }

                                        //add capture move to the list of valid moves
                                        validMoves.add(capture);
                                    }
                                }
                            }

                        }
                    }

                    case KNIGHT -> {
                        for (int offset : piece.getOffsets()) {
                            Square target = Square.getSquare(location.getSquareNumber() + offset);
                            if (target != Square.INVALID) {
                                if (board.isEmpty(target) || !board.getPieceAt(target).isFriendly(color))
                                    validMoves.add(new Move(p, location, target, state.getDiceRoll(), color));
                            }
                        }
                    }

                    case KING -> {
                        for (int offset : piece.getOffsets()) {
                            if (!board.isOffBoard(location.getSquareNumber() + offset)) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);

                                if (board.isEmpty(target) || !board.getPieceAt(target).isFriendly(piece.getColor())) {
                                    validMoves.add(new Move(p, location, target, state.getDiceRoll(), color));
                                }
                            }
                        }
                        //CHECK FOR CASTLING
                        //TODO: check if White can castle before generating the move
                        if (piece.getColor() == Side.WHITE) {
                            if (location == Square.e1) {
                                //SHORT WHITE
                                if (board.isEmpty(location.getSquareRight()) && board.isEmpty(getSquare(6)) && state.isShortCastlingWhite()) {
                                    Move m = new Move(p, location, Square.g1, state.getDiceRoll(), color);
                                    m.castlingRookDestination = f1;
                                    validMoves.add(m);
                                    //TODO: add this type of move gen with flags to all other castling thingies
                                }
                                //LONG WHITE
                                if (board.isEmpty(location.getSquareLeft()) && board.isEmpty(getSquare(2)) && board.isEmpty(getSquare(1)) && state.isLongCastlingWhite()) {
                                    //validMoves.add(new Move(p, location, getSquare(2), state.getDiceRoll(), color));
                                    //state.setCanCastle(true);
                                    Move m = new Move(p, location, Square.c1, state.getDiceRoll(), color);
                                    m.castlingRookDestination = d1;
                                    validMoves.add(m);
                                }
                            }
                            //TODO: check if black can castle before generating the move
                        } else {
                            if (location.getSquareNumber() == 116) {
                                //SHORT BLACK
                                if (board.isEmpty(location.getSquareRight()) && board.isEmpty(getSquare(118)) && state.isShortCastlingBlack()) {
                                    //validMoves.add(new Move(p, location, getSquare(118), state.getDiceRoll(), color));
                                    //state.setCanCastle(true);
                                    Move m = new Move(p, location, Square.g8, state.getDiceRoll(), color);
                                    m.castlingRookDestination = f8;
                                    validMoves.add(m);

                                }
                                //LONG BLACK
                                if (board.isEmpty(location.getSquareLeft()) && board.isEmpty(getSquare(114)) && board.isEmpty(getSquare(113)) && state.isLongCastlingBlack()) {
                                    //validMoves.add(new Move(p, location, getSquare(114), state.getDiceRoll(), color));
                                    //state.setCanCastle(true);
                                    Move m = new Move(p, location, Square.c8, state.getDiceRoll(), color);
                                    m.castlingRookDestination = d8;
                                    validMoves.add(m);
                                }
                            }
                        }
                    }

                    //TODO: Rook case should avoid moving the unmoved rook if castling on that side is still possible
                    case BISHOP, ROOK, QUEEN -> {
                        for (int offset : piece.getOffsets()) {
                            Square target = Square.getSquare(location.getSquareNumber() + offset);

                            while (target != INVALID && board.isEmpty(target)) {
                                validMoves.add(new Move(p, location, target, state.getDiceRoll(), color));
                                target = Square.getSquare(target.getSquareNumber() + offset);
                            }

                            if (target != INVALID && !board.getPieceAt(target).isFriendly(color)) {
                                validMoves.add(new Move(p, location, target, state.getDiceRoll(), color));
                            }
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    public void printPieceAndSquare(List<PieceAndSquareTuple> nodePieceAndSquare) {
        //System.out.println("State; pieceAndSquare: Size: " + nodePieceAndSquare.size());
        //for (PieceAndSquareTuple t : nodePieceAndSquare) {
        //System.out.print(t.toString() + " | ");
        //}
        //printPieceCounts(nodePieceAndSquare);
    }

    public void printPieceCounts(List<PieceAndSquareTuple> pieceAndSquare) {
        int pawn = 0;
        int knight = 0;
        int rook = 0;
        int bishop = 0;
        int king = 0;
        int queen = 0;
//        for (PieceAndSquareTuple t : pieceAndSquare) {
//            if (t.getPiece().equals(Piece.BLACK_QUEEN) || t.getPiece().equals(Piece.WHITE_QUEEN)) {
//                queen++;
//            } else if (t.getPiece().equals(Piece.WHITE_BISHOP) || t.getPiece().equals(Piece.BLACK_BISHOP)) {
//                bishop++;
//            } else if (t.getPiece().equals(Piece.WHITE_KING) || t.getPiece().equals(Piece.BLACK_KING)) {
//                king++;
//            } else if (t.getPiece().equals(Piece.WHITE_ROOK) || t.getPiece().equals(Piece.BLACK_ROOK)) {
//                rook++;
//            } else if (t.getPiece().equals(Piece.WHITE_PAWN) || t.getPiece().equals(Piece.BLACK_PAWN)) {
//                pawn++;
//            } else if (t.getPiece().equals(Piece.WHITE_KNIGHT) || t.getPiece().equals(Piece.BLACK_KNIGHT)) {
//                knight++;
//            }
//        }
        //System.out.println("\nCounts: Pawn: " + pawn + " Knight: " + knight + " Bishop: " + bishop + " Rook: " + rook + " Queen: " + queen + " King: " + king + "\n");
    }

    public Side getColor() {
        return color;
    }
}
