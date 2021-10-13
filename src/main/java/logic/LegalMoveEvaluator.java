package logic;

import gui.PromotionPrompt;
import javafx.application.Platform;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.ArrayList;

import static logic.enums.Piece.WHITE_KING;

public class LegalMoveEvaluator {

    Move move;
    State state;

    /**
     * @param move  move object
     * @param state board state
     * @return true if piece can be moved to tile
     */
    public boolean isLegalMove(Move move, State state) {

        this.move = move;
        this.state = state;

        //can't move piece to the same square the piece is already at
        if (move.getOrigin() == move.getDestination())
            return false;

        //player trying to move opponents piece
        if (move.getPiece().getColor() != move.getSide())
            return false;

        return switch (move.getPiece().getType()) {
            case PAWN -> isLegalPawnMove();
            case KNIGHT -> isLegalKnightMove();
            case BISHOP -> isLegalBishopMove();
            case ROOK -> isLegalRookMove();
            case QUEEN -> isLegalQueenMove();
            case KING -> isLegalKingMove();
            default -> false;
        };
    }

    public boolean isLegalPawnMove() {
        Board b = state.board;

        //check if pawn is trying to move in its own file
        if (b.getFile(move.getOrigin()).contains(move.getDestination())) {
            Square nextSquare = Square.getSquare(move.origin.getSquareNumber() + move.piece.getOffsets()[0]); //can return INVALID
            if (b.isEmpty(nextSquare)) {
                //pawn can only move if next square is empty
                if (nextSquare == move.destination) {
                    //that's the square pawn wants to move to
                    if (move.getPiece().canPromote(nextSquare)) {
                        //pawn is moving into promotion rank
                        if (move.getDiceRoll() != 1 && move.getDiceRoll() != 6) {
                            //if dice roll is not pawn or king, then automatically promote piece
                            move.promotionPiece = move.getPiece().promote(move.getDiceRoll());
                        } else {
                            //ask user what they want to promote to in case of pawn or king dice roll
                            move.promotionPiece = (Piece) Platform.enterNestedEventLoop(new PromotionPrompt(move.getPiece().getColor()));
                        }
                        move.promotionMove = true;
                    }
                    return true;
                } else {
                    //maybe the pawn wanted a double jump
                    if (move.piece.canDoubleJump(move.origin)) {
                        Square nextSquare2 = Square.getSquare(nextSquare.getSquareNumber() + move.piece.getOffsets()[0]);

                        move.enPassant = nextSquare;
                        move.enPassantMove = true;

                        return b.isEmpty(nextSquare2) && nextSquare2 == move.destination;
                    }
                }
            }
        } else {
            //pawn is trying to move to a different file. Only legal if capture
            for (int i = 1; i < 3; i++) {
                Square validTarget = Square.getSquare(move.origin.getSquareNumber() + move.piece.getOffsets()[i]);
                if (validTarget == move.destination) {
                    switch (b.getPieceAt(validTarget)) {
                        case EMPTY -> {
                            if (state.enPassant == move.destination) {
                                move.enPassantCapture = true;
                                return true;
                            }
                        }
                        case OFF_BOARD -> {
                            return false;
                        }
                        default -> {
                            return !b.getPieceAt(validTarget).isFriendly(move.side); //can only capture enemy pieces
                        }
                    }
                }
            }

        }
        return false;
        //TODO pawn promotion
    }

    public boolean isLegalKnightMove() {
        Board b = state.board;
        ArrayList<Square> options = new ArrayList<>();

        options.add(move.getOrigin().getSquareAbove().getLeftUp());
        options.add(move.getOrigin().getSquareAbove().getRightUp());
        options.add(move.getOrigin().getSquareRight().getRightUp());
        options.add(move.getOrigin().getSquareRight().getRightDown());
        options.add(move.getOrigin().getSquareBelow().getLeftDown());
        options.add(move.getOrigin().getSquareBelow().getRightDown());
        options.add(move.getOrigin().getSquareLeft().getLeftUp());
        options.add(move.getOrigin().getSquareLeft().getLeftDown());

        int i = 0;
        while (i < options.size()) {
            if (b.isOffBoard(options.get(i).getSquareNumber())) {
                options.remove(i);
            }
            i++;
        }

        i = 0;
        while (i < options.size()) {
            if (options.get(i) == move.getDestination()) {
                return checkingSides(b, move, move.getDestination());
            }
            i++;
        }
        return false;
    }

    public boolean isLegalQueenMove() {
        Board b = state.board;
        boolean sameFile = move.getOrigin().getFile() == move.getDestination().getFile();
        boolean sameRank = move.getOrigin().getRank() == move.getDestination().getRank();
        boolean sameDiagonal = move.getOrigin().getLeftDiagonals(move.getOrigin()).equals(move.getDestination().getLeftDiagonals(move.getDestination()))
                || move.getOrigin().getRightDiagonals(move.getOrigin()).equals(move.getDestination().getRightDiagonals(move.getDestination()));

        if (sameFile || sameRank || sameDiagonal) {
            if (sameRank) {
                return (checkSameRank(b, move));
            } // true if piece can go there without any obstacle
            else if (sameFile) {
                return checkSameFile(b, move);
            } // true if piece can go there without any obstacle
            else {
                return checkSameDiagonal(b, move);
            } // true if piece can go there without any obstacle
        }
        return false; // meaning not even on same rank, file or diagonal
    }

    public boolean isLegalRookMove() {
        if (State.longCastlingWhite || State.longCastlingBlack || State.longCastlingWhite || State.shortCastlingWhite) {
            if (move.getPiece().getColor() == Side.WHITE) {
                if (move.getOrigin().getSquareNumber() == 0) {
                    State.longCastlingWhite = false;
                    System.out.println("long castling: " + State.longCastlingWhite);
                } else {
                    State.shortCastlingWhite = false;
                    System.out.println("short castling: " + State.shortCastlingWhite);
                }
            } else {
                if (move.getOrigin().getSquareNumber() == 112) {
                    State.longCastlingBlack = false;
                    System.out.println("long castling: " + State.longCastlingBlack);
                } else {
                    State.shortCastlingBlack = false;
                    System.out.println("short castling: " + State.shortCastlingBlack);
                }
            }
        }


        Board b = state.board;
        boolean sameFile = move.getOrigin().getFile() == move.getDestination().getFile();
        boolean sameRank = move.getOrigin().getRank() == move.getDestination().getRank();

        if (sameFile || sameRank) {
            if (sameRank) {
                return (checkSameRank(b, move));
            } // true if piece can go there without any obstacle
            else {
                return checkSameFile(b, move);
            } // true if piece can go there without any obstacle
        }
        return false; // meaning not even on same rank or file
    }

    public boolean isLegalBishopMove() {
        Board b = state.board;
        boolean sameDiagonal = move.getOrigin().getLeftDiagonals(move.getOrigin()).equals(move.getDestination().getLeftDiagonals(move.getDestination()))
                || move.getOrigin().getRightDiagonals(move.getOrigin()).equals(move.getDestination().getRightDiagonals(move.getDestination()));

        if (sameDiagonal) {
            return checkSameDiagonal(b, move); // true if piece can go there without any obstacle
        }
        return false; // meaning not diagonal
    }

    public boolean checkingSides(Board b, Move move, Square currentSquare) {
        return ((b.getPieceAt(currentSquare).getColor() != move.getSide()) && currentSquare == move.getDestination());
    } // all other situations are false, so just implemented one "if clause"

    public boolean checkSameFile(Board b, Move move) {
        int OriginRank = move.getOrigin().getRank();
        int DestRank = move.getDestination().getRank();
        Square currentSquare = move.getOrigin();

        if (OriginRank < DestRank) {
            for (int i = OriginRank; i < DestRank; i++) {
                currentSquare = currentSquare.getSquareAbove();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        } else {
            for (int i = OriginRank; i > DestRank; i--) {
                currentSquare = currentSquare.getSquareBelow();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        }
        return true;
    }

    public boolean checkSameRank(Board b, Move move) {
        int OriginFile = move.getOrigin().getFile();
        int DestFile = move.getDestination().getFile();
        Square currentSquare = move.getOrigin();

        if (OriginFile < DestFile) {
            for (int i = OriginFile; i < DestFile; i++) {
                currentSquare = currentSquare.getSquareRight();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        } else {
            for (int i = OriginFile; i > DestFile; i--) {
                currentSquare = currentSquare.getSquareLeft();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        }
        return true;
    }

    public boolean isLegalKingMove() {
        Board b = state.board;

        Square squareAbove = move.getOrigin().getSquareAbove();
        Square squareBelow = move.getOrigin().getSquareBelow();
        Square squareRight = move.getOrigin().getSquareRight();
        Square squareLeft = move.getOrigin().getSquareLeft();
        Square squareDiagonalRightAbove = move.getOrigin().getRightUp();
        Square squareDiagonalLeftAbove = move.getOrigin().getLeftUp();
        Square squareDiagonalRightBelow = move.getOrigin().getRightDown();
        Square squareDiagonalLeftBelow = move.getOrigin().getLeftDown();

        //check if move is up
        if (squareAbove == move.getDestination()) {
            //disable castlling
            disableCastling();
            //check if square is empty
            if (!b.isEmpty(squareAbove)) {
                return checkingSides(b, move, squareAbove);
            } else
                return true;
        }
        if (squareBelow == move.getDestination()) {
            disableCastling();
            if (!b.isEmpty(squareBelow)) {
                return checkingSides(b, move, squareBelow);
            } else
                return true;
        }
        if (squareRight == move.getDestination()) {
            disableCastling();
            if (!b.isEmpty(squareRight)) {
                return checkingSides(b, move, squareRight);
            } else
                return true;
        }
        if (squareLeft == move.getDestination()) {
            disableCastling();
            if (!b.isEmpty(squareLeft)) {
                return checkingSides(b, move, squareLeft);
            } else
                return true;
        }
        if (squareDiagonalLeftAbove == move.getDestination()) {
            disableCastling();
            if (!b.isEmpty(squareDiagonalLeftAbove)) {
                return checkingSides(b, move, squareDiagonalLeftAbove);
            } else
                return true;
        }
        if (squareDiagonalRightAbove == move.getDestination()) {
            disableCastling();
            if (!b.isEmpty(squareDiagonalRightAbove)) {
                return checkingSides(b, move, squareDiagonalRightAbove);
            } else
                return true;
        }
        if (squareDiagonalLeftBelow == move.getDestination()) {
            disableCastling();
            if (!b.isEmpty(squareDiagonalLeftBelow)) {
                return checkingSides(b, move, squareDiagonalLeftBelow);
            } else
                return true;
        }
        if (squareDiagonalRightBelow == move.getDestination()) {
            disableCastling();
            if (!b.isEmpty(squareDiagonalRightBelow)) {
                return checkingSides(b, move, squareDiagonalRightBelow);
            } else
                return true;
        }

        //check for castling
        if (State.longCastlingWhite || State.longCastlingBlack || State.shortCastlingBlack || State.shortCastlingWhite) {
            if (move.getPiece() == WHITE_KING) {
                if (move.getOrigin().getSquareNumber() == 4) {
                    if (move.getDestination().getSquareNumber() == 6 && b.isEmpty(squareRight) && b.isEmpty(move.getDestination()) && State.shortCastlingWhite) {
                        State.shortCastlingWhite = false;
                        System.out.println("SHORT CASTLING WHITE");
                        return true;
                    } else if (move.getDestination().getSquareNumber() == 2 && b.isEmpty(squareLeft) && b.isEmpty(move.getDestination()) && b.isEmpty(Square.getSquare(1)) && State.longCastlingWhite) {
                        State.longCastlingWhite = false;
                        System.out.println("LONG CASTLING WHITE");
                        return true;
                    }
                }
            } else {
                if (move.getDestination().getSquareNumber() == 118 && b.isEmpty(squareRight) && b.isEmpty(move.getDestination()) && State.shortCastlingBlack) {
                    State.shortCastlingBlack = false;
                    System.out.println("SHORT CASTLING Black");
                    return true;
                } else if (move.getDestination().getSquareNumber() == 114 && b.isEmpty(squareLeft) && b.isEmpty(move.getDestination()) && b.isEmpty(Square.getSquare(113)) && State.longCastlingBlack) {
                    State.longCastlingBlack = false;
                    System.out.println("LONG CASTLING Black");
                    return true;
                }

            }
        }

        return false;
    }

    //disabled castling rights after a king move
    public void disableCastling() {
        if (move.getPiece().getColor() == Side.WHITE) {
            State.shortCastlingWhite = false;
            State.longCastlingWhite = false;
        } else {
            State.shortCastlingBlack = false;
            State.longCastlingBlack = false;
        }
    }


    public boolean checkSameDiagonal(Board b, Move move) {
        int OriginRank = move.getOrigin().getRank();
        int DestRank = move.getDestination().getRank();
        int OriginFile = move.getOrigin().getFile();
        int DestFile = move.getDestination().getFile();

        Square currentSquare = move.getOrigin();
        // either of rank or file can be used here
        if (OriginRank < DestRank && OriginFile < DestFile) { // for right above diagonal
            for (int i = OriginRank; i < DestRank; i++) {
                currentSquare = currentSquare.getSquareRight().getSquareAbove();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        } else if (OriginRank < DestRank && OriginFile > DestFile) { // for left above diagonal
            for (int i = OriginRank; i < DestRank; i++) {
                currentSquare = currentSquare.getSquareLeft().getSquareAbove();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        } else if (OriginRank > DestRank && OriginFile > DestFile) { // for left below diagonal
            for (int i = OriginRank; i > DestRank; i--) {
                currentSquare = currentSquare.getSquareLeft().getSquareBelow();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        } else if (OriginRank > DestRank && OriginFile < DestFile) { // for right below diagonal
            for (int i = OriginRank; i > DestRank; i--) {
                currentSquare = currentSquare.getSquareRight().getSquareBelow();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        }
        return true;
    }
}
