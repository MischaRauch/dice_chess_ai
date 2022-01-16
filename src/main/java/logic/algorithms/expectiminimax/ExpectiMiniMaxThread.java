package logic.algorithms.expectiminimax;

import logic.Move;

public class ExpectiMiniMaxThread extends Thread {

    private Move bestMove;
    private final int depth;
    private final logic.State state;
    private long timeNeeded;
    private boolean isHybrid;

    public ExpectiMiniMaxThread(int depth, logic.State state, boolean isHybrid) {
        this.depth = depth;
        this.state = state;
        this.isHybrid = isHybrid;
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        ExpectiMiniMax expectiMiniMax = new ExpectiMiniMax();
        expectiMiniMax.constructTree(depth, state , isHybrid);
        Move bestMove = expectiMiniMax.getBestMoveForBestNode();
        this.bestMove = bestMove;
        long end = System.nanoTime();
        //System.out.println("ExpectiMiniMaxPlayer: Elapsed Time to generate tree and find optimal move: " + (end - start));
        timeNeeded = end - start;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public long getTimeNeeded() {
        return timeNeeded;
    }


}
