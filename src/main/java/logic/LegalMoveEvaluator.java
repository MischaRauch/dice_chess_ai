package logic;

import logic.board.*;
import logic.enums.Piece;
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
        if (move.getPiece().getColor() != move.getSide())
            return false;

        if (move.getPiece().getType() == Piece.PAWN) {
            return isLegalPawnMove();

        } else if (move.getPiece() == Piece.WHITE_QUEEN || move.getPiece() == Piece.BLACK_QUEEN) {
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


    public boolean isLegalPawnMove() {
         Board b = state.board;

         //check if pawn is trying to move in its own file
         if (b.getFile(move.getOrigin()).contains(move.getDestination())) {
             Square nextSquare = Square.getSquare(move.origin.getSquareNumber() + move.piece.getOffsets()[0]); //can return INVALID
             if (b.isEmpty(nextSquare)) {
                 //pawn can only move if next square is empty
                 if (nextSquare == move.destination) {
                     //that's the square pawn wants to move to
                     return true;
                 } else {
                     //maybe the pawn wanted a double jump
                     if (move.piece.canDoubleJump(move.origin)) {
                         //TODO: update en-passant field
                         Square nextSquare2 = Square.getSquare(nextSquare.getSquareNumber() + move.piece.getOffsets()[0]);
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
                         case EMPTY, OFF_BOARD -> {
                             return false; //TODO en-passant capture still possible if square is empty
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
    public boolean checkingSides (Board b, Move move, Square currentSquare) {
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
                currentSquare = currentSquare.getSquareRight().getSquareAbove();
                if (!b.isEmpty(currentSquare)) {
                    return checkingSides(b, move, currentSquare);
                }
            }
        }
        return true;
    }
}
