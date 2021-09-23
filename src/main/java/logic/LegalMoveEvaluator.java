package logic;

import logic.board.*;

public class LegalMoveEvaluator {

    /**
     * @param move move object
     * @param state board state
     * @return true if piece can be moved to tile
     */
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
