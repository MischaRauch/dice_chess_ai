package logic.algorithms.minimax;

import logic.Move;

public class MiniMaxThread extends Thread {

    private Move bestMove;
    private final int depth;
    private final logic.State state;
    private long timeNeeded;

    public MiniMaxThread(int depth, logic.State state) {
        this.depth = depth;
        this.state = state;
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        MiniMax miniMax = new MiniMax();
        miniMax.constructTree(depth, state);
        Node bestChild = miniMax.findBestChild(true, miniMax.getTree().getRoot().getChildren(),state.getDiceRoll());
        this.bestMove = bestChild.getMove();
        long end = System.nanoTime();
        timeNeeded = (end - start);
        System.out.println("MiniMaxPlayer: Elapsed Time to generate tree and find optimal move: " + (end - start));
    }

    public Move getBestMove() {
        return bestMove;
    }

    public long getTimeNeeded() {
        return timeNeeded;
    }

}

