package logic.player;

import logic.LegalMoveEvaluator;
import logic.Move;
import logic.State;
import logic.algorithms.expectiminimax.ExpectiMiniMax;
import logic.algorithms.expectiminimax.ExpectiMiniMaxThread;
import logic.enums.Side;

public class ExpectiMiniMaxPlayer extends AIPlayer {

    private final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    private final int depth;

    public ExpectiMiniMaxPlayer(int depth, Side color) {
        super(color);
        this.depth = depth;
    }

    @Override
    public Move chooseMove(State state) {
        // java creates a seperate thread to compute minimax while main thread still computing
        ExpectiMiniMaxThread thread = new ExpectiMiniMaxThread(depth,state);
        thread.start();
        try {
            // threads execute code in sequence
            // waits for thread to complete continuing executing this code of choose move
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Move chosenMove = thread.getBestMove();
        // TODO make evalautor not update moves (Phase 3)
        evaluator.isLegalMove(chosenMove, state, true, true);
        System.out.println("ExpectiMiniMaxPlayer: Color: " + this.color.toString() + " Next optimal Move: " + chosenMove);
        return chosenMove;
    }

    @Override
    public String getNameAi() {
        return "ExpectiMiniMax";
    }

}
