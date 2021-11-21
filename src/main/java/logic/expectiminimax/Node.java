package logic.expectiminimax;

import logic.Move;
import logic.State;
import java.util.*;

public class Node {

    private boolean isMaxPlayer;
    private int score;
    private List<Node> children;
    private int diceRoll;
    private int boardEvaluationNumber;
    private State state;
    private Move move;

    // for children
    public Node(boolean isMaxPlayer, int diceRoll, int boardEvaluationNumber, State state, Move move) {
        this.isMaxPlayer = isMaxPlayer;
        this.diceRoll = diceRoll;
        this.boardEvaluationNumber = boardEvaluationNumber;
        this.state = state;
        this.children = new ArrayList<>();
        this.move=move;
    }

    // for root
    public Node(int diceRoll, boolean isMaxPlayer, State state) {
        this.diceRoll=diceRoll;
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

    public int getDiceRoll() {
        return diceRoll;
    }

    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    public State getState() {
        return state;
    }

    public Move getMove() {
        return move;
    }
}
