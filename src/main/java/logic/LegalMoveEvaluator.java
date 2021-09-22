package logic;

public class LegalMoveEvaluator {

    /**
     * @param move move object
     * @param state board state
     * @return true if piece can be moved to tile
     */
    public boolean isLegalMove(Move move, State state) {

        return true;
    }


    /**
     * check if a certain position on board is empty
     * @param pos position of tile to be checked in coordinate location (i.e. e5)
     * @param state board state
     * @return true if empty, false if occupied
     */
    public boolean isEmpty(String pos, State state) {

        return true;
    }

    /**
     * check if there are friendly pieces surrounding it, doesn't work for knight
     * @param pos position of piece to be moved
     * @param state board state
     * @return true if a piece is stuck and is not a knight
     */
    public boolean isBlocked(String pos, State state) {

        return true;
    }


}
