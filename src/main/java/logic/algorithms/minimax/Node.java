package logic.algorithms.minimax;

import logic.Move;
import logic.State;
import java.util.*;

public class Node {

    private boolean isMaxPlayer;
    private List<Node> children;
    private int boardEvaluationNumber;
    private State state;
    private Move move;

    // for children
    public Node(boolean isMaxPlayer, int boardEvaluationNumber, State state, Move move) {
        this.isMaxPlayer = isMaxPlayer;
        this.boardEvaluationNumber = boardEvaluationNumber;
        this.state = state;
        this.children = new ArrayList<>();
        this.move=move;
    }

    // for root
    public Node(boolean isMaxPlayer, State state) {
        this.isMaxPlayer=isMaxPlayer;
        this.state = state;
        this.children = new ArrayList<>();
    }

    public int getBoardEvaluationNumber() {
        return boardEvaluationNumber;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public boolean isMaxPlayer() {
        return isMaxPlayer;
    }

    public List<Node> getChildren() {
        return children;
    }

    public State getState() {
        return state;
    }

    public Move getMove() {
        return move;
    }

}
