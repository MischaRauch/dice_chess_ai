package logic;

import logic.board.*;
import logic.enums.Piece;
import logic.enums.Side;

public class LegalMoveEvaluator {

    /**
     * @param move move object
     * @param state board state
     * @return true if piece can be moved to tile
     */
    public boolean isLegalMove(Move move, State state) {
        Board board = state.board;
        if (move.getPiece() == Piece.WHITE_PAWN) {
            return isLegalPawnMove(move, state);

        }

        return true;
    }

    //idk
    public boolean isLegalPawnMove(Move move,State state) {
         Board b = state.board;

         if (move.getColor() == Side.WHITE) {
             if (b.getPieceAt(move.origin) != Piece.WHITE_PAWN) {
                 //user chose wrong piece/piece color to move -> invalid
                 return false;
             } else {
                 if (move.getOrigin().getSquareAbove() == move.getDestination()) {
                     //the pawn wanted a single move forward
                     //if that square is occupied by friendly piece and we cant go there
                     //or the piece is an empty square or its an opponent, in which case you can capture
                     return !b.getPieceAt(move.getDestination()).isFriendly(move.getColor());
                 } else {
                     //if pawn is on second rank then can jump 2 squares
                     ///TODO: method which returns the rank of the Piece
                     //else just one
                     return true;
                 }
             }
         }

        return true;
    }

}
