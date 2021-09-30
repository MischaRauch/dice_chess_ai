package logic;

import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Square;

public class LegalMoveEvaluator {

    /**
     * @param move move object
     * @param state board state
     * @return true if piece can be moved to tile
     */
    public boolean isLegalMove(Move move, State state) {

        //can't move piece to the same square the piece is already at
        if (move.getOrigin() == move.getDestination())
            return false;

        //player trying to move opponents piece
        if (move.getPiece().getColor() != move.getSide())
            return false;

        if (move.getPiece().getType() == Piece.PAWN) {
            return isLegalPawnMove(move, state);
        }

        return true;
    }


    public boolean isLegalPawnMove(Move move, State state) {
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
                             return !b.getPieceAt(validTarget).isFriendly(move.side);
                         }
                     }
                 }
             }

         }
        return false;
        //TODO pawn promotion
    }
}
