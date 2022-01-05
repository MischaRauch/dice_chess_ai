package logic.player;

import logic.LegalMoveEvaluator;
import logic.ML.DQL;
import logic.Move;
import logic.State;
import logic.enums.Side;

public class QLPlayer extends AIPlayer {
    private final int depth;
    private final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    DQL ql = new DQL();

    public QLPlayer(int depth, Side color) {
        super(color);
        this.depth = depth;
    }

    @Override
    public Move chooseMove(State state) {
        ql.algo(state, state.getColor(), this.depth);
        Move bestMove = ql.getBestMove(state, this.color);
        // TODO make evalautor not update moves (Phase 3)
        evaluator.isLegalMove(bestMove, state, true, true);
        return bestMove;
    }

    @Override
    public String getNameAi() {
        return "QLPlayer";
    }

}
