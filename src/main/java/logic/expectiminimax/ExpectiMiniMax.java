package logic.expectiminimax;

import logic.enums.Piece;
import logic.game.Game;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class ExpectiMiniMax {

    Tree tree;

    public void constructTree(int boardEvaluationNumber) {
        System.out.println("constructTree");
        tree = new Tree();
        Node root = new Node(boardEvaluationNumber, true);
        tree.setRoot(root);
        constructTree(root);
    }

    private void constructTree(Node parentNode) {
        // TODO getPossibleStates
        //List<Piece[][]> listofPossibleHeaps = BoardStateGenerator.getPossibleBoardStates(parentNode.getBoardPieceState());
        List<Integer> listofPossibleHeaps = BoardStateGenerator.getPossibleBoardStatesWeights(parentNode.getBoardPieceState());
        boolean isChildMaxPlayer = !parentNode.isMaxPlayer();
        listofPossibleHeaps.forEach(n -> {
            Node newNode = new Node(n, isChildMaxPlayer);
            parentNode.addChild(newNode);
            if (newNode.getBoardEvaluationNumber() > 0) {
                constructTree(newNode);
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

}
