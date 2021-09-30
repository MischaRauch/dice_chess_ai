package logic;

import logic.board.*;
import logic.enums.Piece;
import logic.enums.Side;
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
        if (move.getPiece().getColor() != move.getColor())
            return false;

        if (move.getPiece() == Piece.WHITE_PAWN) {
            return isLegalPawnMove(move, state);
        }


        if (move.getPiece() == Piece.WHITE_KING || move.getPiece() == Piece.BLACK_KING) {
            return isLegalKingMove(move,state);
        }

        return true;
    }

    //idk
    public boolean isLegalPawnMove(Move move,State state) {
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

    public boolean isLegalKingMove(Move move, State state) {
        Board b = state.board;

        Square squareAbove = move.getOrigin().getSquareAbove();
        Square squareBelow = move.getOrigin().getSquareBelow();
        Square squareRight = move.getOrigin().getSquareRight();
        Square squareLeft = move.getOrigin().getSquareLeft();
        Square squareDiagonalRightAbove = move.getOrigin().getSquareDiagonalRightAbove();
        Square squareDiagonalLeftAbove = move.getOrigin().getSquareDiagonalLeftAbove();
        Square squareDiagonalRightBelow = move.getOrigin().getSquareDiagonalRightBelow();
        Square squareDiagonalLeftBelow = move.getOrigin().getSquareDiagonalLeftBelow();


        //check if move is up
        if (squareAbove == move.getDestination())
            //check if square is empty
            if (b.isEmpty(squareAbove))
                return true;
        if(squareBelow == move.getDestination())
            if (b.isEmpty(squareBelow))
                return true;
        if (squareRight == move.getDestination())
            if (b.isEmpty(squareRight))
                return true;
        if (squareLeft == move.getDestination())
            if (b.isEmpty(squareLeft))
                return true;
        if (squareDiagonalLeftAbove == move.getDestination())
            if (b.isEmpty(squareDiagonalLeftAbove))
                return true;
        if (squareDiagonalRightAbove == move.getDestination())
            if (b.isEmpty(squareDiagonalRightAbove))
                return true;
        if (squareDiagonalLeftBelow == move.getDestination())
            if (b.isEmpty(squareDiagonalLeftBelow))
                return true;
        if(squareDiagonalRightBelow == move.getDestination())
            if (b.isEmpty(squareDiagonalRightBelow))
                return true;

        return false;
    }

}
