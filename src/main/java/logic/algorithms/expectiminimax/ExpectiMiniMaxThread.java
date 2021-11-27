package logic.algorithms.expectiminimax;

import logic.Move;

public class ExpectiMiniMaxThread extends Thread {

    private Move bestMove;
    private int depth;
    private logic.State state;

    public ExpectiMiniMaxThread(int depth, logic.State state) {
        this.depth = depth;
        this.state = state;
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        ExpectiMiniMax expectiMiniMax = new ExpectiMiniMax();
        expectiMiniMax.constructTree(depth, state);
        Move bestMove = expectiMiniMax.getBestMoveForBestNode();
        this.bestMove = bestMove;
        long end = System.nanoTime();
        System.out.println("ExpectiMiniMaxPlayer: Elapsed Time to generate tree and find optimal move: " + (end - start));
    }

    public Move getBestMove() {
        return bestMove;
    }


}
