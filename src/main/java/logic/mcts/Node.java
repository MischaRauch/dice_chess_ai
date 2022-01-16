package logic.mcts;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static logic.mcts.Node.NodeType.*;

public class Node {

    static final double e = 0.1; //something not quite 0 so we avoid division by 0;

    public enum NodeType {DECISION, CHANCE, TERMINAL}

    public boolean isRoot = false;

    Node parent;
    ArrayList<Node> children;
    boolean fullyExpanded = false;
    int roll;
    ArrayList<Integer> validRolls;

    TreeState state;
    Action action;              //action taken to reach this node
    PriorityQueue<Action> validActions;  //all valid actions from this node according to the state
    int actionsTaken;
    double numValidRolls;

    double Q; //quality of node (i.e. total number of wins)
    double N; //Number of visits through this node
    NodeType type; //what kind of node this is
    int diceRoll;

    public Node(Node parent, TreeState state, Action action, NodeType type) {
        this.parent = parent;
        this.state = state;
        this.action = action;
        this.type = type;
        if (type != TERMINAL) {
            Q = 0;
            N = e;
            actionsTaken = 0;
            if (type == CHANCE) {
                validRolls = state.getRolls();
                numValidRolls = validRolls.size();
            }
            validActions = (type == DECISION) ? state.getAvailableActions(parent.getNextRoll()) : null;
            children = new ArrayList<>();
            diceRoll = state.diceRoll;
        } else {
            N = 1;
            fullyExpanded = true;
            //prune siblings
        }
    }

    //root;
    public Node(TreeState state, int roll) {
        this.type = DECISION;
        this.state = state;
        Q = 1;
        N = 1;
        actionsTaken = 0;
        validActions = state.getAvailableActions(roll);
        children = new ArrayList<>();
        isRoot = true;
        this.roll = roll;
    }

    public boolean fullyExpanded() {
        return fullyExpanded;
    }

    public Action getNextAction() {
        Action next = validActions.poll();
        fullyExpanded = validActions.isEmpty();
        return next;
    }

    public int getNextRoll() {
        int roll = validRolls.get(actionsTaken);
        //fullyExpanded = validRolls.isEmpty();
        actionsTaken++; //just gonna reuse this variable lol
        fullyExpanded = actionsTaken == validRolls.size();
        return roll;
    }

    public double getExpectedValue() {
        //average value of children
        switch (type) {
            case DECISION, TERMINAL -> {
                return Q / N;
            }
            //TODO separate out Terminal case

            case CHANCE -> {
                double val = 0.0;
                for (Node child : children) {
                    val += child.getExpectedValue();
                }
                val = val / numValidRolls;
//                val = val / ((double) validRolls.size());
                //val = val / ((double) children.size());
                //System.out.println(val);
                val = (Q / N) / numValidRolls;
                return val;
            }

            default -> {
                return 0;
            }
        }
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void pruneSiblings() {
        parent.children = new ArrayList<>(1);
        parent.children.add(this);
        parent.validActions = new PriorityQueue<>(1);
        parent.validActions.add(this.action);
        fullyExpanded = true;
        parent.fullyExpanded = true;
    }

    @Override
    public String toString() {
        return "[Wins: " + Q + ", Visits: " + N + ", type: " + type + "]";
    }

}
