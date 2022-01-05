package logic.algorithms.minimax;

import dataCollection.CsvHandler;
import logic.Move;
import gui.controllers.ViewDataController;
import java.util.ArrayList;

public class MiniMaxThread extends Thread {

    private Move bestMove;
    private int depth;
    private logic.State state;
    private ArrayList<Long> timeGameList;

    public MiniMaxThread(int depth,logic.State state) {
        this.depth = depth;
        this.state = state;
        timeGameList = new ArrayList<>();
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
        System.out.println("MiniMaxPlayer: " + state.getColor().name() + ", Elapsed Time to generate tree and find optimal move: " + (end - start));
        long time = end - start;
        timeGameList.add(time);
    }

    public Move getBestMove() {
        return bestMove;
    }

    public ArrayList<Long> getTimeGameList(){return timeGameList;}
}

