package logic.player;

import logic.Move;
import logic.State;
import logic.enums.Side;
import logic.algorithms.expectiminimax.ExpectiMiniMax;
import logic.algorithms.expectiminimax.ExpectiMiniMaxThread;

public class ExpectiMiniMaxPlayer extends AIPlayer {

    private final int depth;
    ExpectiMiniMax expectiMiniMax = new ExpectiMiniMax();

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
        state.printPieceAndSquare();
        System.out.println("ExpectiMiniMaxPlayer: Color: " + this.color.toString() + " Next optimal Move: " + chosenMove.toString());
        return chosenMove;
    }

    @Override
    public String getNameAi() {
        return "MiniMax";
    }

}
