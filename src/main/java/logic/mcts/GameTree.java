package logic.mcts;

import logic.enums.Side;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class GameTree {

    private Node root;
    private HashSet<Node> treeNodes; //we assume that there do not exist duplicate nodes (state, action, roll) tuples
    private Side player;

    public GameTree(Node rootNode, Side player) {
        treeNodes = new HashSet<>();
        treeNodes.add(rootNode);
        root = rootNode;
        stateSequence = new LinkedList<>();
        this.player = player;
    }

    public Node getRoot() {
        return root;
    }

    public boolean contains(Node node) {
        return treeNodes.contains(node);
    }

    public void addNode(Node node) {
        treeNodes.add(node);
    }

    public void pruneNode(Node node) {
        treeNodes.remove(node);
    }

    public HashSet<Node> getTreeNodes() {
        return treeNodes;
    }

    public Side getPlayer() {
        return player;
    }


    public List<Node> stateSequence;

    //the Monte Carlo Search Tree

    //perhaps store global hash map of hashed game states to their equivalent node/score found in previous MCTS searches
    //from previous turns. Game states would have to be normalized for both players to be able to use for this to work
    //  if the current game state/node being evaluated is not in the map, then add it
    //  Otherwise load the tree from this point

    //or maybe only add states after game tree has collapsed
    //  This could mean only having to evaluate repetitive quiet moves once


    //we could skip pawns in enemy playout phase unless they can capture or threaten or promote, or have high probability of roll, idk

    //Store MCTS evals for all opening moves in static table


    //For playout phase implement Expectiminimax with Star1 pruning
    //  Perhaps use NN if better results
}
