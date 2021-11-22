package logic.expectiminimax;

import logic.BoardStateAndEvaluationNumberTuple;
import logic.Move;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.ArrayList;
import java.util.List;

public class MiniMax {

    private final boolean DEBUG = false;
    private final BoardStateGenerator gen = new BoardStateGenerator();
    int depth;
    private Tree tree;

    // first recursive method call
    public void constructTree(int depth, State state) {
        this.depth = depth;
        tree = new Tree();
        Node root = new Node(true, state);
        tree.setRoot(root);
        constructTree(root, depth);
    }

    private void constructTree(Node parentNode, int depth) {
        System.out.println("DEPTH " + depth);
        while (this.depth > 0) {
            List<BoardStateAndEvaluationNumberTuple> tupleList = new ArrayList<>();
            // loop through for all 6 dice numbers and generate all possible states
            for (int i = 1; i < 7; i++) {
                // evaluation numbers for i-th dice roll
                List<Integer> possibleEvaluationNumbers = gen.getPossibleBoardStatesWeightsOfSpecificPiece(parentNode.getState().getPieceAndSquare(),
                        parentNode.getState().color, i, parentNode.getState());
                // possible board states for i-th dice roll
                List<List<PieceAndSquareTuple>> possibleBoardStates = gen.getPossibleBoardStates(parentNode.getState().getPieceAndSquare(),
                        parentNode.getState().color, i, parentNode.getState());

                // add this tuple of list of possible eval numbers and board states to tuple list
                tupleList.add(new BoardStateAndEvaluationNumberTuple(possibleBoardStates, possibleEvaluationNumbers));
            }
            boolean isChildMaxPlayer = !parentNode.isMaxPlayer();
            // AI assumes opponent will always choose the most favourable move
            // adding children for min player
            if (isChildMaxPlayer) {
                Node bestChild = parentNode;
                bestChild = addChildrenReturnBestChild(parentNode, tupleList, isChildMaxPlayer, bestChild, Integer.MAX_VALUE);
                this.depth -= 1;
                constructTree(bestChild, this.depth);
            }// adding children for min player
            else if (!isChildMaxPlayer) {
                Node bestChild = parentNode;
                bestChild = addChildrenReturnBestChild(parentNode, tupleList, isChildMaxPlayer, bestChild, Integer.MIN_VALUE);
                this.depth -= 1;
                constructTree(bestChild, this.depth);
            }
        }
    }

    private Node addChildrenReturnBestChild(Node parentNode, List<BoardStateAndEvaluationNumberTuple> tupleList, boolean isChildMaxPlayer,
                                            Node bestChild, int finalEvalNo) {
        for (int j = 0; j < tupleList.size(); j++) {
            // loop through states for pieces
            for (int i = 0; i < tupleList.get(j).getBoardStates().size(); i++) {
                // new state with alterate color
                List<PieceAndSquareTuple> possibleBoardStates = (List<PieceAndSquareTuple>) tupleList.get(j).getBoardStates().get(i);
                int lastPieceIndex = possibleBoardStates.size() - 1;
                int diceNo = Piece.getDiceFromPiece((Piece) possibleBoardStates.get(lastPieceIndex).getPiece());
                State newState = new State(
                        parentNode.getState().getBoard(), diceNo, Side.getOpposite(parentNode.getState().color), parentNode.getState().isApplyCastling(),
                        parentNode.getState().isShortCastlingBlack(), parentNode.getState().isShortCastlingWhite(), parentNode.getState().isLongCastlingBlack(),
                        parentNode.getState().isShortCastlingWhite(), parentNode.getState().castling, possibleBoardStates);

                int evalNo = (int) tupleList.get(diceNo - 1).getEvaluationNumbers().get(i);
                List<PieceAndSquareTuple> prevBoardState = parentNode.getState().getPieceAndSquare();
                PieceAndSquareTuple lastPieceAndSquareTuple = possibleBoardStates.get(lastPieceIndex);
                Square destination = (Square) lastPieceAndSquareTuple.getSquare();
                Piece movingPiece = (Piece) lastPieceAndSquareTuple.getPiece();
                Square origin = null;
                for (PieceAndSquareTuple ps : prevBoardState) {
                    if (ps.getPiece() == movingPiece) {
                        Square previousSquare = (Square) ps.getSquare();
                        if (!containsSquare(possibleBoardStates, previousSquare)) {
                            origin = previousSquare;// we moved from previousSquare
                        }
                    }
                }
                Node newNode = new Node(isChildMaxPlayer, evalNo, newState, new Move(movingPiece, origin, destination, diceNo, movingPiece.getColor()));
                // add each child for each different board state for each different piece type
                // if eval number smaller
                // built in pruning as we only ever add better children, not all children
                if (isChildMaxPlayer && evalNo <= finalEvalNo) {
                    finalEvalNo = evalNo;
                    bestChild = newNode;
                } else if (!isChildMaxPlayer && evalNo >= finalEvalNo) {
                    finalEvalNo = evalNo;
                    bestChild = newNode;
                }
                parentNode.addChild(newNode);
            }
        }
        return bestChild;
    }

    public boolean checkWin() {
        Node root = tree.getRoot();
        checkWin(root);
        return root.getScore() == 1;
    }

    private void checkWin(Node node) {
        List<Node> children = node.getChildren();
        boolean isMaxPlayer = node.isMaxPlayer();
        children.forEach(child -> {
            if (child.getBoardEvaluationNumber() > Math.abs(20000)) {
                child.setScore(isMaxPlayer ? 1 : -1);
            } else {
                checkWin(child);
            }
        });
        Node bestChild = findBestChild(isMaxPlayer, children, node.getState().diceRoll);
        node.setScore(bestChild.getScore());
    }

    private boolean containsSquare(List<PieceAndSquareTuple> state, Square square) {
        Piece[] pieceList = {Piece.WHITE_PAWN, Piece.WHITE_KNIGHT, Piece.WHITE_BISHOP, Piece.WHITE_ROOK, Piece.WHITE_QUEEN, Piece.WHITE_KING,
                Piece.BLACK_PAWN, Piece.BLACK_KNIGHT, Piece.BLACK_BISHOP, Piece.BLACK_ROOK, Piece.BLACK_QUEEN, Piece.BLACK_KING};
        for (int i = 0; i < pieceList.length; i++) {
            for (PieceAndSquareTuple ps : state) {
                PieceAndSquareTuple<Piece, Square> comparitor = new PieceAndSquareTuple(pieceList[i], square);
                if (ps.getPiece() == comparitor.getPiece() && ps.getSquare() == comparitor.getSquare()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Tree getTree() {
        return tree;
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

    public Node findBestChild(boolean isMaxPlayer, List<Node> children, int diceRoll) {
        Node bestChild = null;
        if (isMaxPlayer) {
            int max = Integer.MIN_VALUE;
            for (Node child : children) {
                // remove child.getDiceRoll()==diceRoll) to make OP :) 100% win rate
                if (child.getBoardEvaluationNumber() > max && child.getState().diceRoll == diceRoll) {
                    max = child.getBoardEvaluationNumber();
                    bestChild = child;
                }
            }
        } else {
            int min = Integer.MAX_VALUE;
            for (Node child : children) {
                // remove child.getDiceRoll()==diceRoll) to make OP :) 100% win rate
                if (child.getBoardEvaluationNumber() < min && child.getState().diceRoll == diceRoll) {
                    min = child.getBoardEvaluationNumber();
                    bestChild = child;
                }
            }
        }
        return bestChild;
    }

    private void printEvaluationNumbers(List<Integer> possibleEvaluationNumbers) {
        // for visual appeal
        if (possibleEvaluationNumbers.size() != 0) {
            System.out.println("ExpectiMiniMax; possibleEvaluationNumbers: ");
            for (int i = 0; i < possibleEvaluationNumbers.size(); i++) {
                System.out.print(possibleEvaluationNumbers.get(i) + " ");
            }
            System.out.println();
        }
    }

    private void printBoardStates(List<List<PieceAndSquareTuple>> possibleBoardStates, int diceRoll) {
        System.out.println("MiniMax; DICE ROLL: " + diceRoll);
        // for visual appeal
        if (possibleBoardStates.size() != 0) {
            System.out.println("ExpectiMiniMax; possibleBoardStates: Size: " + possibleBoardStates.size());
            for (int i = 0; i < possibleBoardStates.size(); i++) {
                System.out.println("ExpectiMiniMax; Piece Type: " + Piece.getNeutralPieceFromDice(diceRoll).toString());
                printPieceAndSquare(possibleBoardStates.get(i));
            }
            System.out.println();
        }
    }

    public void printPieceAndSquare(List<PieceAndSquareTuple> pieceAndSquare) {
        System.out.println(" Minimax; pieceAndSquare: Size: " + pieceAndSquare.size());
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
            if (t.getPiece().equals(Piece.BLACK_QUEEN) || t.getPiece().equals(Piece.WHITE_QUEEN)) {
                queen++;
            } else if (t.getPiece().equals(Piece.WHITE_BISHOP) || t.getPiece().equals(Piece.BLACK_BISHOP)) {
                bishop++;
            } else if (t.getPiece().equals(Piece.WHITE_KING) || t.getPiece().equals(Piece.BLACK_KING)) {
                king++;
            } else if (t.getPiece().equals(Piece.WHITE_ROOK) || t.getPiece().equals(Piece.BLACK_ROOK)) {
                rook++;
            } else if (t.getPiece().equals(Piece.WHITE_PAWN) || t.getPiece().equals(Piece.BLACK_PAWN)) {
                pawn++;
            } else if (t.getPiece().equals(Piece.WHITE_KNIGHT) || t.getPiece().equals(Piece.BLACK_KNIGHT)) {
                knight++;
            }
        }
        System.out.println("\nCounts: Pawn: " + pawn + " Knight: " + knight + " Bishop: " + bishop + " Rook: " + rook + " Queen: " + queen + " King: " + king + "\n");
    }

}
