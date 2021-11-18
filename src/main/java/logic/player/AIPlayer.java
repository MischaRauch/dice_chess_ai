package logic.player;

import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.Dice;
import logic.Move;
import logic.State;


import java.util.LinkedList;
import java.util.List;

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

    public abstract Move chooseMove(State state) throws CloneNotSupportedException;

    //have not tested this
    //need to incorporate castling and promotion
    public List<Move> getValidMoves(State state) {
        List<Move> validMoves = new LinkedList<>();
        Piece piece = Dice.diceToPiece[state.diceRoll - 1].getColoredPiece(color);

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
                            validMoves.add(new Move(p, location, naturalMove, state.diceRoll, color));

                            //double jumping
                            Square doubleJump = Square.getSquare(naturalMove.getSquareNumber() + piece.getOffsets()[0]);
                            if (doubleJump != Square.INVALID && board.isEmpty(doubleJump) && piece.canDoubleJump(location))
                                validMoves.add(new Move(p, location, doubleJump, state.diceRoll, color));
                        }

                        for (int k = 1; k < 3; k++) {
                            if (!board.isOffBoard(location.getSquareNumber() + piece.getOffsets()[k])) {
                                Square validTarget = Square.getSquare(location.getSquareNumber() + piece.getOffsets()[k]);

                                if (board.getPieceAt(validTarget) != EMPTY && !board.getPieceAt(validTarget).isFriendly(piece.getColor()))
                                    validMoves.add(new Move(p, location, validTarget, state.diceRoll, color));
                            }
                        }
                    }

                    case KNIGHT, KING -> {
                        for (int offset : piece.getOffsets()) {
                            if (!board.isOffBoard(location.getSquareNumber() + offset)) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);

                                if (board.isEmpty(target) || !board.getPieceAt(target).isFriendly(piece.getColor())) {
                                    validMoves.add(new Move(p, location, target, state.diceRoll, color));
                                }
                            }
                        }
                    }

                    case BISHOP, ROOK, QUEEN -> {
                        for (int offset : piece.getOffsets()) {
                            if (!board.isOffBoard(location.getSquareNumber() + offset)) {
                                Square target = Square.getSquare(location.getSquareNumber() + offset);

                                while (target != INVALID && board.isEmpty(target) ) {
                                    validMoves.add(new Move(p, location, target, state.diceRoll, color));
                                    target = Square.getSquare(target.getSquareNumber() + offset);
                                }

                                if (target != INVALID && !board.getPieceAt(target).isFriendly(piece.getColor())) {
                                    validMoves.add(new Move(p, location, target, state.diceRoll, color));
                                }
                            }
                        }
                    }
                }
            }

        }

        return validMoves;
    }
}
