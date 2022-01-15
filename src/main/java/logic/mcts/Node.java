package logic.mcts;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import static logic.mcts.Node.NodeType.*;

public class Node {

    static final double e = 0.1; //something not quite 0 so we avoid division by 0;

    public enum NodeType {DECISION, CHANCE, TERMINAL}

    public boolean isRoot = false;

    Node parent;
    List<Node> children;
    boolean fullyExpanded = false;

    List<Integer> validRolls;

    TreeState state;
    Action action;              //action taken to reach this node
    PriorityQueue<Action> validActions;  //all valid actions from this node according to the state
    int actionsTaken;

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
            validRolls = (type == CHANCE) ? state.getRolls() : null;
            validActions = (type == DECISION) ? state.getAvailableActions(parent.getNextRoll()) : null;
            children = new LinkedList<>();
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
        children = new LinkedList<>();
        isRoot = true;
        this.roll = roll;
    }

    int roll;

    public boolean fullyExpanded() {

        return fullyExpanded;
//        if (type == TERMINAL)
//            return true;
//        if (type == DECISION)
//            return fullyExpanded;
//            //return actionsTaken == validActions.size();
//        if (type == CHANCE)
//            return fullyExpanded;
        //return children.size() == validRolls.size();

        //System.out.println("NOT FULLY EXPANDED");
        //return false;
    }

    public Action getNextAction() {
        Action next = validActions.poll();
        fullyExpanded = validActions.isEmpty();
        return next;
    }

    public int getNextRoll() {
        if (fullyExpanded) {
            //System.out.println("FULLY CHANCE EXPANDED");
            return validRolls.get(MCTSAgent.random.nextInt(validRolls.size()));
        }
        int roll = validRolls.get(actionsTaken);
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
                double val = 0;
                for (Node child : children) {
                    val += child.getExpectedValue();
                }
                val = val / ((double) validRolls.size());
                return val;
            }
        }

        return 0;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void pruneSiblings() {
        parent.children.clear();
        parent.children.add(this);
        parent.validActions.clear();
        parent.validActions.add(this.action);
        fullyExpanded = true;
        parent.fullyExpanded = true;
    }

    @Override
    public String toString() {
        return "[Wins: " + Q + ", Visits: " + N + ", type: " + type + "]";
    }

}
