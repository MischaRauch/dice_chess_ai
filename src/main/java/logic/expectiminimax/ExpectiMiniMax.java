package logic.expectiminimax;

import logic.*;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.List;
import java.util.Map;

import java.util.*;
import java.util.stream.Collectors;

import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;

public class ExpectiMiniMax {

    private Tree tree;
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // AI assumes opponent will always choose the most favourable move
            // adding children for min player
            if(isChildMaxPlayer) {
                System.out.println("Adding children for min player");
                int finalEvalNo = Integer.MAX_VALUE;
                Node bestChild = null;
                System.out.println("llistOfAllPossibleBoardStates.size(): " + listOfAllPossibleBoardStates.size());
                // loop through generated states
                for (int j = 0; j < listOfAllPossibleBoardStates.size(); j++) {
                    System.out.println("Index of generated States looping : " + j);
                    //System.out.println("pieces to be evaluated: child: is max:" + isChildMaxPlayer + p.toString());
                    // loop through states for pieces
                    System.out.println("listOfAllPossibleBoardStates.get(j).size(): " + listOfAllPossibleBoardStates.get(j).size());
                    for (int i = 0; i < listOfAllPossibleBoardStates.get(j).size(); i++) {
                        System.out.println("Index of generated States looping : " + j);

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
                        System.out.println("totalTupleList.get(j) size: " + totalTupleList.get(0).size());
                        System.out.println("dice roll number: " + diceNo);
                        System.out.println("eval numbers: " + totalTupleList.get(0).get(diceNo-1).getEvaluationNumbers().toString());
                        int evalNo = (int) totalTupleList.get(0).get(diceNo-1).getEvaluationNumbers().get(i);

                        List<PieceAndSquareTuple> currentBoardState = listOfAllPossibleBoardStates.get(j).get(i);
                        List<PieceAndSquareTuple> prevBoardState = parentNode.getState().getPieceAndSquare();
                        PieceAndSquareTuple lastPieceAndSquareTuple = (PieceAndSquareTuple) currentBoardState.get(lastPieceIndex);
                        Square destination = (Square) lastPieceAndSquareTuple.getSquare();
                        Piece movingPiece = (Piece) lastPieceAndSquareTuple.getPiece();
                        Square origin = null;

                        System.out.println("Containsssss: " + containsSquare(currentBoardState,Square.a1));

                        for (PieceAndSquareTuple ps : prevBoardState) {
                            if (ps.getPiece()==movingPiece) { //if black horse
                                System.out.println("Moving piece: " + movingPiece.toString());
                                // get it's square
                                Square previousSquare = (Square) ps.getSquare(); //black horse square in previous state
                                System.out.println("Current square: " + previousSquare);
                                if(containsSquare(currentBoardState,previousSquare)) {
                                    //didn't move
                                } else {
                                    // we moved from previousSquare
                                    origin=previousSquare;
                                }
                            }
                        }
                        System.out.println("DEPTH: " + this.depth + " origin square " + origin.toString());
                        System.out.println("DEPTH: " + this.depth + " dest square (always correct) " + destination.toString());

                        Node newNode = new Node(isChildMaxPlayer, diceNo, evalNo, newState,
                                new Move(movingPiece, origin, destination, diceNo, movingPiece.getColor()));
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
                System.out.println("Adding children for max player");
                int finalEvalNo = Integer.MIN_VALUE;
                Node bestChild = null;
                System.out.println("llistOfAllPossibleBoardStates.size(): " + listOfAllPossibleBoardStates.size());
                for (int j = 0; j < listOfAllPossibleBoardStates.size(); j++) {
                    //System.out.println("pieces to be evaluated: child: is max:" + isChildMaxPlayer + p.toString());
                    // loop through states for pieces
                    System.out.println("listOfAllPossibleBoardStates.get(j).size(): " + listOfAllPossibleBoardStates.get(j).size());
                    int index = 0;
                    for (int i = 0; i < listOfAllPossibleBoardStates.get(j).size(); i++) {
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
                        System.out.println("totalTupleList.get(j) size: " + totalTupleList.get(0).size());
                        System.out.println("dice roll number: " + diceNo);
                        System.out.println("eval numbers: " + totalTupleList.get(0).get(diceNo-1).getEvaluationNumbers().toString());
                        int evalNo = (int) totalTupleList.get(0).get(diceNo-1).getEvaluationNumbers().get(i);

                        List<PieceAndSquareTuple> currentBoardState = listOfAllPossibleBoardStates.get(j).get(i);
                        List<PieceAndSquareTuple> prevBoardState = parentNode.getState().getPieceAndSquare();
                        PieceAndSquareTuple lastPieceAndSquareTuple = (PieceAndSquareTuple) currentBoardState.get(lastPieceIndex);
                        Square destination = (Square) lastPieceAndSquareTuple.getSquare();
                        Piece movingPiece = (Piece) lastPieceAndSquareTuple.getPiece();
                        Square origin = null;

                        System.out.println("Containsssss: " + containsSquare(currentBoardState,Square.a1));

                        for (PieceAndSquareTuple ps : prevBoardState) {
                            if (ps.getPiece()==movingPiece) { //if black horse
                                System.out.println("Moving piece: " + movingPiece.toString());
                                // get it's square
                                Square previousSquare = (Square) ps.getSquare(); //black horse square in previous state
                                System.out.println("Current square: " + previousSquare);
                                if(containsSquare(currentBoardState,previousSquare)) {
                                    //didn't move
                                } else {
                                    // we moved from previousSquare
                                    origin=previousSquare;
                                }
                            }
                        }
                        System.out.println("DEPTH: " + this.depth + " origin square " + origin.toString());
                        System.out.println("DEPTH: " + this.depth + " dest square (always correct) " + destination.toString());

                        Node newNode = new Node(isChildMaxPlayer, diceNo, evalNo, newState,
                                new Move(movingPiece, origin, destination, diceNo, movingPiece.getColor()));
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

    private boolean containsSquare(List<PieceAndSquareTuple> state, Square square) {
        Piece[] pieceList = {Piece.WHITE_PAWN,Piece.WHITE_KNIGHT, Piece.WHITE_BISHOP,Piece.WHITE_ROOK,Piece.WHITE_QUEEN,Piece.WHITE_KING,
                Piece.BLACK_PAWN,Piece.BLACK_KNIGHT, Piece.BLACK_BISHOP,Piece.BLACK_ROOK,Piece.BLACK_QUEEN,Piece.BLACK_KING};
        for (int i = 0; i < pieceList.length; i++) {
            for (PieceAndSquareTuple ps : state) {
                PieceAndSquareTuple<Piece,Square> comparitor = new PieceAndSquareTuple(pieceList[i],square);
                if (ps.getPiece()==comparitor.getPiece() && ps.getSquare()==comparitor.getSquare()) {
                    return true;
                }
            }
        }
        return false;
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

//    static int miniMax(Node node, int depth, boolean is_max) {
//        // Condition for Terminal node
//        if (tree.getRoot()==node || node.getState().gameOver!=0) {
//            return node.getBoardEvaluationNumber();
//        }
//
//        // Maximizer node. Chooses the max from the
//        // left and right sub-trees
//        if (is_max) {
//            int maxEval = Integer.MIN_VALUE;
//            // choose max from children
//            for (Node child : node.getChildren()) {
//                // recursive call to check children of children
//                int eval = miniMax(child,depth-1,false);
//                maxEval = Math.max(maxEval,eval);
//            }
//            return maxEval;
//        } else {
//            int minEval = Integer.MAX_VALUE;
//            // choose min from children
//            for (Node child : node.getChildren()) {
//                // recursive call to check children of children
//                int eval = miniMax(child,depth-1,false);
//                minEval = Math.min(minEval,eval);
//            }
//            return minEval;
//        }
//    }

    private static int getMaxFromList(List<Node> children) {
        int max = Integer.MIN_VALUE;
        for (Node n : children) {
            if (n.getBoardEvaluationNumber() > max) {
                max=n.getBoardEvaluationNumber();
            }
        }
        return max;
    }

    public Node findBestChild(boolean isMaxPlayer, List<Node> children) {
        Node bestChild = null;
        if (isMaxPlayer) {
            int max = Integer.MIN_VALUE;
            for (Node child : children) {
                if (child.getBoardEvaluationNumber()>max) {
                    max=child.getBoardEvaluationNumber();
                    bestChild=child;
                }
            }
        } else {
            int min = Integer.MAX_VALUE;
            for (Node child : children) {
                if (child.getBoardEvaluationNumber()<min) {
                    min=child.getBoardEvaluationNumber();
                    bestChild=child;
                }
            }
        }
        return bestChild;
    }

}
