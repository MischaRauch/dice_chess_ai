package logic.expectiminimax;

import logic.Dice;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import java.util.List;
import java.util.Map;

import java.util.*;
import java.util.stream.Collectors;

public class ExpectiMiniMax {

    private static Tree tree;
    int depth;

    // first recursive method call
    public void constructTree(int depth,State state,Side aiSide) {
        System.out.println("constructTree");
        System.out.println("DEPTH " + depth);
        this.depth = depth;
        tree = new Tree();
        Node root = new Node(aiSide, state.diceRoll, true, state);
        System.out.println("ExpectiMiniMax; Side : " + aiSide.toString() + " diceroll : " + state.diceRoll);
        tree.setRoot(root);
        constructTree(root,depth);
    }

    private void constructTree(Node parentNode,int depth) {
        BoardStateGenerator gen = new BoardStateGenerator();
        boolean isChildMaxPlayer = !parentNode.isMaxPlayer();
        System.out.println("DEPTH " + depth);

        Map<Piece,List<Integer>> evalNumMap = new HashMap<>();
        Map<Piece,List<List<PieceAndSquareTuple>>> boardStateMap = new HashMap<>();

        // loop through for all 6 dice numbers
        for (int i = 0; i < 6; i++) {
            // evlation numbers for one dice roll
            List<Integer> possibleEvaluationNumbers =
                    gen.getPossibleBoardStatesWeights(parentNode.getState().getPieceAndSquare(),parentNode.getColor(),i+1,parentNode.getState());

            // possible board states for all dice rolls
            List<List<PieceAndSquareTuple>> possibleBoardStates =
                    gen.getPossibleBoardStates(parentNode.getState().getPieceAndSquare(),parentNode.getColor(),i+1,parentNode.getState());

            System.out.println("Piece.getPieceFromDice(i,parentNode.getColor()) " + Piece.getPieceFromDice(i+1,parentNode.getColor()));
            print(possibleEvaluationNumbers);

            if (evalNumMap.get(Piece.getPieceFromDice(i+1,parentNode.getColor()))!=null) {
                evalNumMap.put(Piece.getPieceFromDice(i+1,parentNode.getColor()),possibleEvaluationNumbers);
                boardStateMap.put(Piece.getPieceFromDice(i+1,parentNode.getColor()),possibleBoardStates);
            }
        }
        System.out.println("evalNumMap size" + evalNumMap.size());
        System.out.println("PieceAndSquareTuple size" + boardStateMap.size());
        System.out.println("isChildMaxPlayer " + isChildMaxPlayer);
        // AI assumes opponent will always choose the most favourable move
        // children are min
        if(isChildMaxPlayer==false) {
            int evalNumber = Integer.MAX_VALUE;
            Node bestChild=null;
            for (Piece p : evalNumMap.keySet()) {
                System.out.println("pieces to be evaluated: child: is max:" + isChildMaxPlayer + p.toString());
                // eval numbers
                for (int i = 0; i < evalNumMap.get(p).size(); i++) {
                    Node newNode = new Node(isChildMaxPlayer, Side.getOpposite(parentNode.getColor()),
                            Piece.getDiceFromPiece(p), evalNumMap.get(p).get(i), parentNode.getState());
                    // add each child for each different board state for each different piece type
                    // if eval number smaller
                    if (evalNumMap.get(p).get(i) < evalNumber) {
                        evalNumber = evalNumMap.get(p).get(i);
                        System.out.println("isChildMaxPlayer " + isChildMaxPlayer + " evalNumber " + evalNumber);
                        bestChild = newNode;
                    }
                    parentNode.addChild(newNode);
                }
            }
            constructTree(bestChild,--depth);
        //children are max
        } else if (isChildMaxPlayer==true) {
            int evalNumber = Integer.MIN_VALUE;
            Node bestChild=null;
            for (Piece p : evalNumMap.keySet()) {
                System.out.println("pieces to be evaluated: child: is max:" + isChildMaxPlayer + p.toString());
                // eval numbers
                for (int i = 0; i < evalNumMap.get(p).size(); i++) {
                    Node newNode = new Node(isChildMaxPlayer, Side.getOpposite(parentNode.getColor()),
                            Piece.getDiceFromPiece(p), evalNumMap.get(p).get(i), parentNode.getState());
                    // add each child for each different board state for each different piece type
                    // if eval number bigger
                    if (evalNumMap.get(p).get(i) > evalNumber) {
                        evalNumber = evalNumMap.get(p).get(i);
                        System.out.println("isChildMaxPlayer " + isChildMaxPlayer + " evalNumber " + evalNumber);
                        bestChild = newNode;
                    }
                    parentNode.addChild(newNode);
                }
            }
            constructTree(bestChild,--depth);
        }
    }


//        State temp = new State(state.getBoard(),parentNode.getDiceRoll(),parentNode.getColor());
//
//        for (int i = 0; i < possibleEvaluationNumbers.size(); i++) {
//            // will pass state with castling
//            State temp = new State(state.getBoard(),parentNode.getDiceRoll(),parentNode.getColor());
//
//            for (int j = 0; j < diceNumbers.size(); j++) {
//                Node newNode = new Node(isChildMaxPlayer,Side.getOpposite(parentNode.getColor()),
//                        diceNumbers.get(j),possibleEvaluationNumbers.get(i), temp);
//                parentNode.addChild(newNode);
//                if (depth!=0 && i==possibleEvaluationNumbers.size()-1) {
//                    System.out.println("constructTree 2");
//                    constructTree(newNode,--depth,newNode.getState());
//                }
//            }
//
//        }

//    public boolean checkWin() {
//        Node root = tree.getRoot();
//        checkWin(root);
//        return root.getScore() == 1;
//    }
//
//    private void checkWin(Node node) {
//        List<Node> children = node.getChildren();
//        boolean isMaxPlayer = node.isMaxPlayer();
//        // lose condition
//        children.forEach(child -> {
//            if (child.getBoardEvaluationNumber() < 0) {
//                child.setScore(isMaxPlayer ? 1 : -1);
//            } else {
//                checkWin(child);
//            }
//        });
//        Node bestChild = findBestChild(isMaxPlayer, children);
//        node.setScore(bestChild.getScore());
//    }

//    private Node findBestChild(boolean isMaxPlayer, List<Node> children) {
//        Comparator<Node> byScoreComparator = Comparator.comparing(Node::getScore);
//        return children.stream()
//                .max(isMaxPlayer ? byScoreComparator : byScoreComparator.reversed())
//                .orElseThrow(NoSuchElementException::new);
//    }

    public Tree getTree() {
        return tree;
    }

    private void print(List<Integer> possibleEvaluationNumbers) {
        System.out.println("possibleEvaluationNumbers: ");
        for (int i = 0; i < possibleEvaluationNumbers.size(); i++) {
            System.out.print(possibleEvaluationNumbers.get(i) + " ");
        }
        System.out.println();
    }

//    private void printPossibleEvaluationNumbersMap(Map<Piece,Integer> possibleEvaluationNumbers) {
//        System.out.println("ExpectiMiniMax; possibleEvaluationNumbers: ");
//        for (Piece p : possibleEvaluationNumbers.keySet()) {
//            System.out.println("Piece: " + p.toString() + " evaluation number: " + possibleEvaluationNumbers.get(p).toString());
//        }
//    }
//
//    private void printPossibleBoardStatesMap(Map<Piece,List<PieceAndSquareTuple>> possibleBoardStates) {
//        System.out.println("ExpectiMiniMax; possibleBoardStates: ");
//        for (Piece p : possibleBoardStates.keySet()) {
//            System.out.println("Piece: " + p.toString() + " evaluation number: " + possibleBoardStates.get(p).toString());
//        }
//    }

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
