package logic.player;
import java.util.Random;
import logic.Move;
import logic.State;
import logic.enums.Side;

import java.util.List;

public class RandomMovesPlayer extends AIPlayer {

    private final static Random randomly = new Random();
    public RandomMovesPlayer(Side color) {
        super(color);
    }

    @Override
    public Move chooseMove(State state) {
        List<Move> validMoves = getValidMoves(state);
        //etc
        //choose which move to use here
        //etc
        return validMoves.get(randomly.nextInt(validMoves.size()));
    }

}
