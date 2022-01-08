package logic.player;

import logic.LegalMoveEvaluator;
import logic.Move;
import logic.State;
import logic.algorithms.minimax.MiniMax;
import logic.algorithms.minimax.MiniMaxThread;
import logic.enums.Side;
public class MiniMaxPlayer extends AIPlayer {

    private final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
    private final int depth;
    MiniMax miniMax = new MiniMax();

    public MiniMaxPlayer(int depth, Side color) {
        super(color);
        this.depth = depth;
    }

    @Override
    public Move chooseMove(State state) {
        // java creates a seperate thread to compute minimax while main thread still computing
        MiniMaxThread thread = new MiniMaxThread(depth,state);
        // before thread :      94305800, 46340000, 37389000
        // after first thread:  71309300, 20369700, 16032500
        // thread also fixed game freezing
        thread.start();
        try {
            // threads execute code in sequence
            // waits for thread to complete continuing executing this code of choose move
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Move chosenMove = thread.getBestMove();
        System.out.println("MiniMaxPlayer: Color: " + this.color.toString() + " Next optimal Move: " + chosenMove);
        // TODO make evalautor not update moves
        // to promote en passant moves
        evaluator.isLegalMove(chosenMove, state, true, true);
        //state.printPieceAndSquare();
        return chosenMove;
    }

    @Override
    public String getNameAi() {
        return "MiniMax";
    }

}
