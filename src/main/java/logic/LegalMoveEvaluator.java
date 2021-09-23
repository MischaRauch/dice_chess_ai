package logic;

import logic.board.*;

public class LegalMoveEvaluator {


    public boolean isLegalMove(Move move, State state) {
        Board board = state.board;
        if (move.getPiece() == Piece.WHITE_PAWN) {
            Square above = board.getSquareAbove(move.getOrigin());
            if (board.isEmpty(above)) {
                //nlbbalsblabalb
            }

        }

        return true;
    }


}
