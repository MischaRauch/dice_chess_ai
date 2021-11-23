package logic.minimax;

import logic.Move;

public class MiniMaxThread extends Thread {

    private Move bestMove;
    private int depth;
    private logic.State state;

    public MiniMaxThread(int depth,logic.State state) {
        this.depth = depth;
        this.state = state;
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        MiniMax miniMax = new MiniMax();
        miniMax.constructTree(depth, state);
        Node bestChild = miniMax.findBestChild(true, miniMax.getTree().getRoot().getChildren(),state.getDiceRoll());
        System.out.println("Optimal Move: " + bestChild.getMove().toString());
        this.bestMove = bestChild.getMove();
        long end = System.nanoTime();
        System.out.println("MiniMaxPlayer: Elapsed Time to generate tree and find optimal move: " + (end - start));
    }

    public Move getBestMove() {
        return bestMove;
    }

}

