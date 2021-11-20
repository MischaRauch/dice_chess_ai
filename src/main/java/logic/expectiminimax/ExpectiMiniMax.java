package logic.expectiminimax;

import logic.BoardStateAndEvaluationNumberTuple;
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
    public void constructTree(int depth,State state) {
        System.out.println("constructTree");
        System.out.println("DEPTH " + depth);
        this.depth = depth;
        tree = new Tree();
        Node root = new Node(state.diceRoll, true, state);
        System.out.println("ExpectiMiniMax; Side : " + state.color.toString() + " diceroll : " + state.diceRoll);
        tree.setRoot(root);
        constructTree(root,depth);
    }

    private void constructTree(Node parentNode,int depth) {
        System.out.println("THIS DEPTH " + this.depth);
        System.out.println("DEPTH " + depth);
        while(this.depth>0) {
            BoardStateGenerator gen = new BoardStateGenerator();
            System.out.println("DEPTH " + depth);

            List<List<BoardStateAndEvaluationNumberTuple>> totalTupleList = new ArrayList<>();
            List<BoardStateAndEvaluationNumberTuple> tupleList = new ArrayList<>();

            List<List<List<PieceAndSquareTuple>>> listOfAllPossibleBoardStates = new ArrayList<>();
            List<List<Integer>> listOfAllPossibleEvaluationNumbers = new ArrayList<>();
            System.out.println("tupleList size: " + tupleList.size());
            System.out.println("listOfAllPossibleBoardStates size: " + listOfAllPossibleBoardStates.size());
            System.out.println("listOfAllPossibleEvaluationNumbers size: " + listOfAllPossibleEvaluationNumbers.size());

            // loop through for all 6 dice numbers
            for (int i = 1; i < 7; i++) {
                System.out.println("Dice Number: " + i);
                // evlation numbers for i dice roll
                ;
                List<Integer> possibleEvaluationNumbers =
                        gen.getPossibleBoardStatesWeights(parentNode.getState().getPieceAndSquare(),
                                parentNode.getState().color,i,parentNode.getState());
                System.out.println("parent node piece and square :");
                printPieceAndSquare(parentNode.getState().getPieceAndSquare());
                // possible board states fir ith dice roll
                List<List<PieceAndSquareTuple>> possibleBoardStates =
                        gen.getPossibleBoardStates(parentNode.getState().getPieceAndSquare(),
                                parentNode.getState().color, i,parentNode.getState());

                printEvaluationNumbers(possibleEvaluationNumbers);
                printBoardStates(possibleBoardStates, i);

                if(!possibleBoardStates.isEmpty()) {
                    listOfAllPossibleBoardStates.add(possibleBoardStates);
                }
                if(!listOfAllPossibleEvaluationNumbers.isEmpty()) {
                    listOfAllPossibleEvaluationNumbers.add(possibleEvaluationNumbers);
                }
                tupleList.add(new BoardStateAndEvaluationNumberTuple((List<List<PieceAndSquareTuple>>)possibleBoardStates,
                        (List<Integer>)possibleEvaluationNumbers));
            }
            totalTupleList.add(tupleList);

            boolean isChildMaxPlayer = !parentNode.isMaxPlayer();

            // AI assumes opponent will always choose the most favourable move
            // adding children for min player
            if(isChildMaxPlayer) {
                int finalEvalNo = Integer.MAX_VALUE;
                Node bestChild = null;
                // loop through generated states
                for (int j = 0; j < listOfAllPossibleBoardStates.size()-1; j++) {
                    //System.out.println("pieces to be evaluated: child: is max:" + isChildMaxPlayer + p.toString());
                    // loop through states for pieces
                    for (int i = 0; i < listOfAllPossibleBoardStates.get(j).size()-1; i++) {
                        System.out.println("Index of all possible board states for all pieces with valid moves: " + j);
                        // new state with alterate color
                        int lastPieceIndex = listOfAllPossibleBoardStates.get(j).get(i).size()-1;
                        int diceNo = Piece.getDiceFromPiece((Piece)listOfAllPossibleBoardStates.get(j).get(i).get(lastPieceIndex).getPiece());

                        State newState = new State(
                                parentNode.getState().getBoard(),
                                diceNo,
                                Side.getOpposite(parentNode.getState().color),
                                parentNode.getState().isApplyCastling(),
                                parentNode.getState().isShortCastlingBlack(),
                                parentNode.getState().isShortCastlingWhite(),
                                parentNode.getState().isLongCastlingBlack(),
                                parentNode.getState().isShortCastlingWhite(),
                                parentNode.getState().castling,
                                parentNode.getState().getPieceAndSquare()
                        );
                        System.out.println("Index of generated State for given piece : " + i);
                        int evalNo = (int) tupleList.get(j).getEvaluationNumbers().get(i);
                        //int evalNo = (int)totalTupleList.get(totalTupleList.size()-this.depth).get(j).getEvaluationNumbers().get(i);
                        Node newNode = new Node(isChildMaxPlayer, diceNo, evalNo, newState);
                        // add each child for each different board state for each different piece type
                        // if eval number smaller
                        // built in pruning as we only ever add better children, not all children
                        if (evalNo < finalEvalNo) {
                            finalEvalNo = evalNo;
                            System.out.println("isChildMaxPlayer " + isChildMaxPlayer + " finalEvalNo " + finalEvalNo);
                            bestChild = newNode;
                        }
                        parentNode.addChild(newNode);
                    }
                }
                this.depth-=1;
                constructTree(bestChild,this.depth);
                // adding children for max player
            } else if (!isChildMaxPlayer) {
                int finalEvalNo = Integer.MIN_VALUE;
                Node bestChild = null;
                for (int j = 0; j < listOfAllPossibleBoardStates.size()-1; j++) {
                    System.out.println("Index of all possible board states for all pieces with valid moves: " + j);

                    //System.out.println("pieces to be evaluated: child: is max:" + isChildMaxPlayer + p.toString());
                    // loop through states for pieces
                    System.out.println("listOfAllPossibleBoardStates.get(j).size(): " + listOfAllPossibleBoardStates.get(j).size());
                    for (int i = 0; i < listOfAllPossibleBoardStates.get(j).size()-1; i++) {
                        // new state with alterate color
                        int lastPieceIndex = listOfAllPossibleBoardStates.get(j).get(i).size()-1;
                        int diceNo = Piece.getDiceFromPiece((Piece) listOfAllPossibleBoardStates.get(j).get(i).get(lastPieceIndex).getPiece());
                        State newState = new State(
                                parentNode.getState().getBoard(),
                                diceNo,
                                Side.getOpposite(parentNode.getState().color),
                                parentNode.getState().isApplyCastling(),
                                parentNode.getState().isShortCastlingBlack(),
                                parentNode.getState().isShortCastlingWhite(),
                                parentNode.getState().isLongCastlingBlack(),
                                parentNode.getState().isShortCastlingWhite(),
                                parentNode.getState().castling,
                                parentNode.getState().getPieceAndSquare()
                        );
                        System.out.println("Index of generated State for given piece : " + i);
                        int evalNo = (int) tupleList.get(j).getEvaluationNumbers().get(i);
                        System.out.println(evalNo);
                        // used to identify what piece the moves where generated for
                        System.out.println(diceNo);

                        Node newNode = new Node(isChildMaxPlayer, diceNo, evalNo, newState);
                        // add each child for each different board state for each different piece type
                        // if eval number smaller
                        // built in pruning as we only ever add better children, not all children
                        if (evalNo > finalEvalNo) {
                            finalEvalNo = evalNo;
                            System.out.println("isChildMaxPlayer " + isChildMaxPlayer + " finalEvalNo " + finalEvalNo);
                            bestChild = newNode;
                        }
                        parentNode.addChild(newNode);
                    }
                }
                this.depth-=1;
                constructTree(bestChild,this.depth);
            }
        }
    }

    public Tree getTree() {
        return tree;
    }

    private void printEvaluationNumbers(List<Integer> possibleEvaluationNumbers) {
        System.out.println("ExpectiMiniMax; possibleEvaluationNumbers: ");
        for (int i = 0; i < possibleEvaluationNumbers.size(); i++) {
            System.out.print(possibleEvaluationNumbers.get(i) + " ");
        }
        System.out.println();
    }

    private void printBoardStates(List<List<PieceAndSquareTuple>> possibleBoardStates, int diceRoll) {
        System.out.println("ExpectiMiniMax; possibleBoardStates: Size: " + possibleBoardStates.size());
        for (int i = 0; i < possibleBoardStates.size(); i++) {
            System.out.println("ExpectiMiniMax; Piece Type: " + Piece.getNeutralPieceFromDice(diceRoll).toString());
                printPieceAndSquare(possibleBoardStates.get(i));
        }
        System.out.println();
    }

    public void printPieceAndSquare(List<PieceAndSquareTuple> pieceAndSquare){
        System.out.println(" BoardStateGenerator; pieceAndSquare: Size: " + pieceAndSquare.size());
        for (PieceAndSquareTuple t : pieceAndSquare) {
            System.out.print(t.toString() + " | ");
        }
        printPieceCounts(pieceAndSquare);
    }

    public void printPieceCounts(List<PieceAndSquareTuple> pieceAndSquare) {
        int pawn = 0;
        int knight = 0;
        int rook = 0;
        int bishop = 0;
        int king = 0;
        int queen = 0;
        for (PieceAndSquareTuple t : pieceAndSquare) {
            if(t.getPiece().equals(Piece.BLACK_QUEEN)||t.getPiece().equals(Piece.WHITE_QUEEN)) {
                queen++;
            }else if (t.getPiece().equals(Piece.WHITE_BISHOP)||t.getPiece().equals(Piece.BLACK_BISHOP)) {
                bishop++;
            }else if (t.getPiece().equals(Piece.WHITE_KING)||t.getPiece().equals(Piece.BLACK_KING)) {
                king++;
            }else if (t.getPiece().equals(Piece.WHITE_ROOK)||t.getPiece().equals(Piece.BLACK_ROOK)) {
                rook++;
            }else if (t.getPiece().equals(Piece.WHITE_PAWN)||t.getPiece().equals(Piece.BLACK_PAWN)) {
                pawn++;
            }else if (t.getPiece().equals(Piece.WHITE_KNIGHT)||t.getPiece().equals(Piece.BLACK_KNIGHT)) {
                knight++;
            }
        }
        System.out.println("\nCounts: Pawn: " + pawn + " Knight: " + knight + " Bishop: " + bishop + " Rook: " + rook + " Queen: " + queen + " King: " + king + "\n");
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
