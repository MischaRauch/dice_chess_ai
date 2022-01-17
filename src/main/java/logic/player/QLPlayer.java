package logic.player;

import logic.LegalMoveEvaluator;
import logic.ML.DQL;
import logic.Move;
import logic.State;
import logic.enums.Side;

import java.util.ArrayList;

public class QLPlayer extends AIPlayer {
    private final int depth;
    private final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    DQL ql = new DQL();
    private long timeNeeded;
    ArrayList<Integer> avgTime;

    public QLPlayer(int depth, Side color) {
        super(color);
        this.depth = depth;
        avgTime = new ArrayList<>();
    }

    @Override
    public Move chooseMove(State state) {
        long start = System.nanoTime();
        long startTime = System.currentTimeMillis();
        ql.algo(state, state.getColor(), this.depth);
        Move bestMove = ql.getBestMove(state, this.color);
        long end = System.nanoTime();
        // TODO make evalautor not update moves (Phase 3)
        evaluator.isLegalMove(bestMove, state, true, true);
        timeNeeded = end - start;
        //System.out.println("QLPlayer: Color: " + this.color.toString() + " Next optimal Move: " + bestMove);
        long endTime = System.currentTimeMillis();
        //System.out.println("That took " + (endTime - startTime) + " milliseconds");
        avgTime.add((int) (endTime - startTime));
        //System.out.println("Currently avg time taken" + getAvg(avgTime));
        return bestMove;
    }

    public int getAvg(ArrayList<Integer> list) {
        int total = 0;
        for (Integer value: list) {
            total += value;
        }
        return total / list.size();
    }

    @Override
    public String getNameAi() {
        return "QLPlayer";
    }

    @Override
    public long getTimeNeeded() {
        return timeNeeded;
    }

}
