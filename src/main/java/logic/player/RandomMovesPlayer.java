package logic.player;

import logic.enums.Side;
import logic.Move;
import logic.State;

import java.util.List;
import java.util.Random;

public class RandomMovesPlayer extends AIPlayer {

    private final static Random randomly = new Random();
    public RandomMovesPlayer(Side color) {
        super(color);
    }

    @Override
    public Move chooseMove(State state) {
        List<Move> validMoves = getValidMoves(state);
        return validMoves.get(randomly.nextInt(validMoves.size()));
    }

}
