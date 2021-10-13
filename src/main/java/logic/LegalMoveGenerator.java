package logic;

import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.enums.Validity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static logic.enums.Piece.WHITE_KING;

public class LegalMoveGenerator {

    Move move;
    State state;
    //state
    //take pos on board (x and y) or (square), take piece type
    //return string of moves
// getLegalMoves(origin, piecetype) return square[]

    //

    public ArrayList<Square> getLegalMoves(State state, Square squareOrigin, Piece piece, Side side) {
        LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
        ArrayList<Square> legalMoves = new ArrayList<>();
        //fundamentally wrong with move as it need findal destinatino
        //will need to loop through destinations, can't loop through destinations as can't "set" Squares
//        for (int i = 0; i < 128; i++) {
//            if(evaluator.isLegalMove(new Move(piece,squareOrigin,Square.getSquare(i),1,side),state)==true) {
//                legalMoves.add(Square.getSquare(i));
//                System.out.println("added index : " + Square.getSquare(i).getBoardIndex());
//            }
//        }
        for (int file = 0; file < 7; file++) {
            for (int rank = 0; rank < 7; rank++) {
                if(evaluator.isLegalMove(new Move(piece,squareOrigin,Square.getSquare(rank,file),1,side),state)==true) {
                    legalMoves.add(Square.getSquare(rank,file));
                    System.out.println("loop rank: " + rank + " - loop file: " + file);
                    System.out.println("square rank: " + Square.getSquare(rank,file).getRank() +
                            " - square file: " + Square.getSquare(rank,file).getFile());
                }
            }
        }
        return legalMoves;
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
        if (Game.longCastlingWhite || Game.longCastlingBlack || Game.shortCastlingBlack || Game.shortCastlingWhite) {
            if (move.getPiece() == WHITE_KING) {
                if (move.getOrigin().getSquareNumber() == 4) {
                    if (move.getDestination().getSquareNumber() == 6 && b.isEmpty(squareRight) && b.isEmpty(move.getDestination()) && Game.shortCastlingWhite) {
                        Game.shortCastlingWhite = false;
                        System.out.println("SHORT CASTLING WHITE");
                        return true;
                    } else if (move.getDestination().getSquareNumber() == 2 && b.isEmpty(squareLeft) && b.isEmpty(move.getDestination()) && b.isEmpty(Square.getSquare(1)) && Game.longCastlingWhite) {
                        Game.longCastlingWhite = false;
                        System.out.println("LONG CASTLING WHITE");
                        return true;
                    }
                }
            } else {
                if (move.getDestination().getSquareNumber() == 118 && b.isEmpty(squareRight) && b.isEmpty(move.getDestination()) && Game.shortCastlingBlack) {
                    Game.shortCastlingBlack = false;
                    System.out.println("SHORT CASTLING Black");
                    return true;
                } else if (move.getDestination().getSquareNumber() == 114 && b.isEmpty(squareLeft) && b.isEmpty(move.getDestination()) && b.isEmpty(Square.getSquare(113)) && Game.longCastlingBlack) {
                    Game.longCastlingBlack = false;
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
            Game.shortCastlingWhite = false;
            Game.longCastlingWhite = false;
        } else {
            Game.shortCastlingBlack = false;
            Game.longCastlingBlack = false;
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
