package logic;

import logic.board.*;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

public class LegalMoveEvaluator {

    Move move;
    State state;

    /**
     * @param move move object
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
        if (move.getPiece().getColor() != move.getColor())
            return false;

        if (move.getPiece() == Piece.WHITE_PAWN) {
            return isLegalPawnMove();
        }

        else if (move.getPiece() == Piece.WHITE_QUEEN || move.getPiece() == Piece.BLACK_QUEEN) {
            return isLegalQueenMove();
        }

        else if (move.getPiece() == Piece.WHITE_ROOK || move.getPiece() == Piece.BLACK_ROOK) {
            return isLegalRookMove();
        }

        else if (move.getPiece() == Piece.WHITE_BISHOP || move.getPiece() == Piece.BLACK_BISHOP) {
            return isLegalBishopMove();
        }
        return true;
    }

    //idk
    public boolean isLegalPawnMove() {
        Board b = state.board;
        //check if pawn is trying to move in its own file
        if (b.getFile(move.getOrigin()).contains(move.getDestination())) {
            if (move.getColor() == Side.WHITE) {
                //white pawns can only move up
                Square squareAbove = move.getOrigin().getSquareAbove();
                if (b.isEmpty(squareAbove)) {
                    //make sure square above pawn is empty
                    if (squareAbove == move.getDestination()) {
                        //the pawn wanted a single move forward
                        return true;
                    }  else if (move.getOrigin().getRank() == 2) {
                        //pawn is eligible for a double jump
                        //TODO: update en passant field
                        return squareAbove.getSquareAbove() == move.getDestination() && b.isEmpty(squareAbove.getSquareAbove());
                    } else return false;
                } else {
                    //square above pawn is not empty, so this pawn cannot move
                    return false;
                }
            } else {
                //black pawn can only move "down"
                Square squareBelow = move.getOrigin().getSquareBelow();
                if (b.isEmpty(squareBelow)) {
                    //make sure square below pawn is empty
                    //pawn wants to do a double jump
                    if (squareBelow == move.getDestination()) {
                        //the pawn wanted a single move forward
                        return true;
                    } else if (move.getOrigin().getRank() == 7) {
                        //pawn is eligible for a double jump
                        //TODO: update en passant field
                        return squareBelow.getSquareBelow() == move.getDestination() && b.isEmpty(squareBelow.getSquareBelow());
                    } else return false;
                } else {
                    //square below pawn is not empty, so this pawn cannot move
                    return false;
                }
            }
        } else {
            //pawn is trying to move to a different file. Only legal if capture
            //TODO: check pawn capture
            return false;
        }
    }

    public boolean isLegalQueenMove() {
        Board b = state.board;
        boolean sameFile = move.getOrigin().getFile() == move.getDestination().getFile();
        boolean sameRank = move.getOrigin().getRank() == move.getDestination().getRank();
        boolean sameDiagonal = move.getOrigin().getLeftDiagonals(move.getOrigin()).equals(move.getDestination().getLeftDiagonals(move.getDestination()))
                || move.getOrigin().getRightDiagonals(move.getOrigin()).equals(move.getDestination().getRightDiagonals(move.getDestination()));

        if (sameFile || sameRank || sameDiagonal) {
            if (sameRank) {return (checkSameRank(b, move));} // true if piece can go there without any obstacle
            else if (sameFile) {return checkSameFile(b, move);} // true if piece can go there without any obstacle
            else { return checkSameDiagonal(b, move);} // true if piece can go there without any obstacle
        }
        return false; // meaning not even on same rank, file or diagonal
    }

    public boolean isLegalRookMove() {
        Board b = state.board;
        boolean sameFile = move.getOrigin().getFile() == move.getDestination().getFile();
        boolean sameRank = move.getOrigin().getRank() == move.getDestination().getRank();

        if (sameFile || sameRank) {
            if (sameRank) {return (checkSameRank(b, move));} // true if piece can go there without any obstacle
            else {return checkSameFile(b, move);} // true if piece can go there without any obstacle
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

    public boolean checkSameFile(Board b, Move move) {
        int OriginRank = move.getOrigin().getRank();
        int DestRank = move.getDestination().getRank();
        Square currentSquare = move.getOrigin();

        if (OriginRank < DestRank) {
            for (int i = OriginRank; i < DestRank; i++) {
                currentSquare = currentSquare.getSquareAbove();
                if (!b.isEmpty(currentSquare)) {
                    return false;
                }
            }
        } else {
            for (int i = OriginRank; i > DestRank; i--) {
                currentSquare = currentSquare.getSquareBelow();
                if (!b.isEmpty(currentSquare)) {
                    return false;
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
                    return false;
                }
            }
        } else {
            for (int i = OriginFile; i > DestFile; i--) {
                currentSquare = currentSquare.getSquareLeft();
                if (!b.isEmpty(currentSquare)) {
                    return false;
                }
            }
        }
        return true;
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
                    return false;
                }
            }
        } else if (OriginRank < DestRank && OriginFile > DestFile) { // for left above diagonal
            for (int i = OriginRank; i < DestRank; i++) {
                currentSquare = currentSquare.getSquareLeft().getSquareAbove();
                if (!b.isEmpty(currentSquare)) {
                    return false;
                }
            }
        } else if (OriginRank > DestRank && OriginFile > DestFile) { // for left below diagonal
            for (int i = OriginRank; i > DestRank; i--) {
                currentSquare = currentSquare.getSquareLeft().getSquareBelow();
                if (!b.isEmpty(currentSquare)) {
                    return false;
                }
            }
        } else if (OriginRank > DestRank && OriginFile < DestFile) { // for right below diagonal
            for (int i = OriginRank; i > DestRank; i--) {
                currentSquare = currentSquare.getSquareRight().getSquareAbove();
                if (!b.isEmpty(currentSquare)) {
                    return false;
                }
            }
        }
        return true;
    }

}
