package logic.player;

import logic.Dice;
import logic.Move;
import logic.State;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.LinkedList;
import java.util.List;

import static logic.enums.Piece.EMPTY;
import static logic.enums.Square.INVALID;
import static logic.enums.Square.getSquare;


public abstract class AIPlayer {

    Side color;

    public AIPlayer(Side color) {
        this.color = color;
    }

    public AIPlayer() {
        this(Side.BLACK);
    }

    public abstract Move chooseMove(State state);

    //have not tested this
    //need to incorporate castling and promotion
    public List<Move> getValidMoves(State state) {
        List<Move> validMoves = new LinkedList<>();
        Piece piece = Dice.diceToPiece[state.diceRoll - 1].getColoredPiece(color);

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
                        //this one is more complex and weird since it depends on logic.board state with the en passant and capturing
                        Square naturalMove = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[0]);
                        if (naturalMove != Square.INVALID && board.isEmpty(naturalMove)) {
                            Move natural = new Move(p, location, naturalMove, state.diceRoll, color);

                            if (p.canPromote(naturalMove)) {
                                //pawn is moving into promotion rank
                                if (state.diceRoll != 1 && state.diceRoll != 6) {
                                    //if dice roll is not pawn or king, then automatically promote piece
                                    natural.promotionPiece = p.promote(state.diceRoll);
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
                            if (doubleJump != Square.INVALID && board.isEmpty(doubleJump) && piece.canDoubleJump(location))
                                validMoves.add(new Move(p, location, doubleJump, state.diceRoll, color));

                        }

                        for (int k = 1; k < 3; k++) {
                            Square captureTarget = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[k]);
                            if (captureTarget != Square.INVALID && board.getPieceAt(captureTarget) != EMPTY && !board.getPieceAt(captureTarget).isFriendly(color)) {
                                Move capture = new Move(p, location, captureTarget, state.diceRoll, color);

                                if (p.canPromote(captureTarget)) {
                                    //pawn is moving into promotion rank
                                    if (state.diceRoll != 1 && state.diceRoll != 6) {
                                        //if dice roll is not pawn or king, then automatically promote piece
                                        capture.promotionPiece = p.promote(state.diceRoll);
                                    } else {
                                        //auto promote to Queen
                                        //TODO: potentially give AI the choice to promote to knight in cases where it would lead to a king capture in the next turn
                                        capture.promotionPiece = Piece.QUEEN.getColoredPiece(color);
                                    }

                                    capture.promotionMove = true;
                                }

                                validMoves.add(capture);
                            }

                        }
                    }

                    //TODO: seperate King into different case in order to incorporate Castling Generation and possibly check escape
                    case KNIGHT -> {
                        for (int offset : piece.getOffsets()) {
                            Square target = Square.getSquare(location.getSquareNumber() + offset);
                            if (target != Square.INVALID) {
                                if (board.isEmpty(target) || !board.getPieceAt(target).isFriendly(color))
                                    validMoves.add(new Move(p, location, target, state.diceRoll, color));
                            }

                        }
                    }

                    case KING -> {
                        for (int offset : piece.getOffsets()) {
                            if (!board.isOffBoard(location.getSquareNumber() + offset)) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);

                                if (board.isEmpty(target) || !board.getPieceAt(target).isFriendly(piece.getColor())) {
                                    validMoves.add(new Move(p, location, target, state.diceRoll, color));
                                }
                            }
                        }
                        //CHECK FOR CASTLING
                        if (piece.getColor() == Side.WHITE) {
                            if (location.getSquareNumber() == 4) {
                                //SHORT WHITE
                                if (board.isEmpty(location.getSquareRight()) && board.isEmpty(getSquare(6)) && state.isShortCastlingWhite()) {
                                    validMoves.add(new Move(p, location, getSquare(6), state.diceRoll, color));
                                }
                                //LONG WHITE
                                if (board.isEmpty(location.getSquareLeft()) && board.isEmpty(getSquare(2)) && board.isEmpty(getSquare(1)) && state.isLongCastlingWhite()) {
                                    validMoves.add(new Move(p, location, getSquare(2), state.diceRoll, color));
                                }
                            }
                        }
                        else {
                            if (location.getSquareNumber() == 116) {
                                //SHORT BLACK
                                if (board.isEmpty(location.getSquareRight()) && board.isEmpty(getSquare(118)) && state.isShortCastlingBlack()) {
                                    validMoves.add(new Move(p, location, getSquare(118), state.diceRoll, color));
                                }
                                //LONG BLACK
                                if (board.isEmpty(location.getSquareLeft()) && board.isEmpty(getSquare(114)) && board.isEmpty(getSquare(113)) && state.isLongCastlingBlack()) {
                                    validMoves.add(new Move(p, location, getSquare(114), state.diceRoll, color));
                                }
                            }
                        }
                    }

                    //TODO: Rook case should avoid moving the unmoved rook if castling on that side is still possible
                    case BISHOP, ROOK, QUEEN -> {
                        for (int offset : piece.getOffsets()) {
                            Square target = Square.getSquare(location.getSquareNumber() + offset);

                            while (target != INVALID && board.isEmpty(target)) {
                                validMoves.add(new Move(p, location, target, state.diceRoll, color));
                                target = Square.getSquare(target.getSquareNumber() + offset);
                            }

                            if (target != INVALID && !board.getPieceAt(target).isFriendly(color)) {
                                validMoves.add(new Move(p, location, target, state.diceRoll, color));
                            }
                        }
                    }
                }
            }

        }

        return validMoves;
    }
}
