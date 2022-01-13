package logic.mcts;

import java.util.LinkedList;
import java.util.List;

import static logic.mcts.Node.NodeType.DECISION;
import static logic.mcts.Node.NodeType.TERMINAL;

public class Node {

    static final double e = 0.1; //something not quite 0 so we avoid division by 0;

    public enum NodeType {DECISION, CHANCE, TERMINAL}

    public boolean isRoot = false;

    Node parent;
    List<Node> children;

    List<Integer> validRolls;

    TreeState state;
    Action action;              //action taken to reach this node
    List<Action> validActions;  //all valid actions from this node according to the state
    int actionsTaken;

    double Q; //quality of node (i.e. total number of wins)
    double N; //Number of visits through this node
    NodeType type; //what kind of node this is

    public Node(Node parent, TreeState state, Action action, NodeType type) {
        this.parent = parent;
        this.state = state;
        this.action = action;
        this.type = type;
        if (type != TERMINAL) {
            Q = 0;
            N = e;
            actionsTaken = 0;
            validRolls = (type == NodeType.CHANCE) ? state.getRolls() : null;
            validActions = (type == DECISION) ? state.getAvailableActions(parent.getNextRoll()) : null;
            children = new LinkedList<>();
        } else {
            N = 1;
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
    }

    public boolean fullyExpanded() {
        if (type == TERMINAL)
            return true;
        if (type == DECISION)
            return actionsTaken == validActions.size();
        if (type == NodeType.CHANCE)
            return children.size() == validRolls.size();

        //System.out.println("NOT FULLY EXPANDED");
        return false;
    }

    public Action getNextAction() {
        //if (fullyExpanded()) {
        //System.out.println("FULLY DECISION EXPANDED");
        //}
        Action next = validActions.get(actionsTaken);
        actionsTaken++;
        return next;
    }

    public int getNextRoll() {
        if (fullyExpanded()) {
            //System.out.println("FULLY CHANCE EXPANDED");
            return validRolls.get(MCTSAgent.random.nextInt(validRolls.size()));
        }
        int roll = validRolls.get(actionsTaken);
        actionsTaken++; //just gonna reuse this variable lol
        return roll;
    }

    public double getExpectedValue() {
        //average value of children
        switch (type) {
            case DECISION, TERMINAL -> {
                return Q / N;
            }

            case CHANCE -> {
                double val = 0;
                for (Node child : children) {
                    val += child.getExpectedValue();
                }
                val = val / ((double) validRolls.size()); //should actually be the number of dice rolls available};
                return val;
            }
        }


        return 0;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    @Override
    public String toString() {
        String s = "[Wins: " + Q + ", Visits: " + N + ", type: " + type + "]";
        return s;
    }

//    public Node applyAction(Node p, Action a) {
//        //here we apply an action to the parent nodes state in order to obtain a new child node
//        switch (p.type) {
//            case ROOT, DECISION -> {}//we would create a chance node
//            case CHANCE -> {} //we would need to roll the die and create a decision node
//            case LEAF -> {} //we would need to expand the node or something idk
//            case TERMINAL -> {} //we can't really apply actions to terminal states
//        }
//
//        return new Node();
//    }
}
