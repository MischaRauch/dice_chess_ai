package logic.player;

import logic.LegalMoveEvaluator;
import logic.Move;
import logic.State;
import logic.enums.Side;

import java.util.List;
import java.util.Random;

// based on https://towardsdatascience.com/implementing-a-chess-engine-from-scratch-be38cbdae91

public class RandomMovesPlayer extends AIPlayer {

    private final static Random randomly = new Random();
    private long timeNeeded;
    private final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();

    public RandomMovesPlayer(Side color) {
        super(color);
    }

    @Override
    public Move chooseMove(State state) {
        long start = System.nanoTime();
        List<Move> validMoves = getValidMoves(state);
        Move chosenMove = validMoves.get(randomly.nextInt(validMoves.size()));
        long end = System.nanoTime();
        timeNeeded = end - start;
        return chosenMove;
    }

    @Override
    public String getNameAi() {
        return "Random AI";
    }

    @Override
    public long getTimeNeeded() {
        return timeNeeded;
    }

}
