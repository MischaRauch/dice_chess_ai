package logic.expectiminimax;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.max;

public class ExpectiMiniMax {

    private static Tree tree;
    int depth;

    public void constructTree(int boardEvaluationNumber,int depth) {
        System.out.println("constructTree");
        this.depth = depth;
        tree = new Tree();
        Node root = new Node(boardEvaluationNumber, true);
        tree.setRoot(root);
        constructTree(root,depth);
    }

    private void constructTree(Node parentNode,int depth) {
        List<Integer> listofPossibleHeaps = BoardStateGenerator.getPossibleBoardStatesWeights(parentNode.getBoardPieceState());
        System.out.println("listofPossibleHeaps: " + Arrays.toString(listofPossibleHeaps.toArray()));
        boolean isChildMaxPlayer = !parentNode.isMaxPlayer();

        int max = max(listofPossibleHeaps);
        System.out.println("Max: " + max);

        depth--;
        int finalDepth = depth;

        listofPossibleHeaps.forEach(n -> {
            Node newNode = new Node(n, isChildMaxPlayer);
            parentNode.addChild(newNode);;
            if (newNode.getBoardEvaluationNumber() > max || finalDepth ==0) {
                System.out.println("Constructing new tree : ");
                constructTree(newNode, finalDepth);
            }

        });
    }

    public boolean checkWin() {
        Node root = tree.getRoot();
        checkWin(root);
        return root.getScore() == 1;
    }

    private void checkWin(Node node) {
        List<Node> children = node.getChildren();
        boolean isMaxPlayer = node.isMaxPlayer();
        // lose condition
        children.forEach(child -> {
            if (child.getBoardEvaluationNumber() < 0) {
                child.setScore(isMaxPlayer ? 1 : -1);
            } else {
                checkWin(child);
            }
        });
        Node bestChild = findBestChild(isMaxPlayer, children);
        node.setScore(bestChild.getScore());
    }

    private Node findBestChild(boolean isMaxPlayer, List<Node> children) {
        Comparator<Node> byScoreComparator = Comparator.comparing(Node::getScore);
        return children.stream()
                .max(isMaxPlayer ? byScoreComparator : byScoreComparator.reversed())
                .orElseThrow(NoSuchElementException::new);
    }

    public Tree getTree() {
        return tree;
    }


//    static float expectimax(Node node, boolean is_max)
//    {
//        // Condition for Terminal node
//        if (tree.getRoot()==node) {
//            return node.getBoardEvaluationNumber();
//        }
//
//        // Maximizer node. Chooses the max from the
//        // left and right sub-trees
//        if (is_max) {
//            return Math.max(
//                    expectimax(
//                            node.left, false),
//                    expectimax(node.right, false));
//        }
//
//        // Chance node. Returns the average of
//        // the left and right sub-trees
//        else {
//            return (float) ((
//                    expectimax(node.left, true)
//                            + expectimax(node.right, true))
//                    / 2.0);
//        }
//    }

}
