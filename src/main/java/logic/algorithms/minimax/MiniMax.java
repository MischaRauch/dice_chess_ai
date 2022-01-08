package logic.algorithms.minimax;

import logic.BoardStateAndEvaluationNumberTuple;
import logic.Move;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.algorithms.BoardStateGenerator;
import logic.enums.Piece;
import logic.enums.Square;

import java.util.ArrayList;
import java.util.List;

public class MiniMax {

    private final boolean CheckOutPruning = false;
    private final BoardStateGenerator gen = new BoardStateGenerator();
    private int depth;
    private Tree tree;
    private int initialDepth;
    private Node bestNode;

    // first recursive method call
    public void constructTree(int depth, State state) {
        this.depth = depth;
        tree = new Tree();
        Node root = new Node(true, state);
        tree.setRoot(root);
        constructTree(root, depth);
        initialDepth = depth;
    }

    private void constructTree(Node parentNode, int depth) {
        if (depth == 0) bestNode = parentNode;
        while (this.depth > 0) {
            List<BoardStateAndEvaluationNumberTuple> allStatesAndBoardEvaluationsForGivenPieceType = new ArrayList<>();
            // loop through for all 6 dice numbers and generate all possible states
            for (int i = 1; i < 7; i++) {
                // evaluation numbers for i-th dice roll
                List<Integer> possibleEvaluationNumbersForGivenPiece = gen.getPossibleBoardStatesWeightsOfSpecificPiece(parentNode.getState().getPieceAndSquare(),
                        parentNode.getState().getColor(), i, parentNode.getState());
                // possible board states for i-th dice roll
                List<List<PieceAndSquareTuple>> possibleBoardStatesForGivenPiece = gen.getPossibleBoardStates(parentNode.getState().getPieceAndSquare(),
                        parentNode.getState().getColor(), i, parentNode.getState());

                // add this tuple of list of possible eval numbers and board states to tuple list
                allStatesAndBoardEvaluationsForGivenPieceType.add(new BoardStateAndEvaluationNumberTuple(possibleBoardStatesForGivenPiece, possibleEvaluationNumbersForGivenPiece));
            }
            boolean isChildMaxPlayer = !parentNode.isMaxPlayer();
            // AI assumes opponent will always choose the most favourable move
            // adding children for min player
            // Optimization: only construct tree with the best child, and not arbitrary or every child, and when adding children to node only add children that are better than previous
            // so we have this built in pruning effect that's semi-recursive
            if (isChildMaxPlayer) {
                Node bestChild = parentNode;
                bestChild = addChildrenReturnBestChild(parentNode, allStatesAndBoardEvaluationsForGivenPieceType, isChildMaxPlayer, bestChild, Integer.MAX_VALUE);
                bestNode = bestChild;
                this.depth -= 1;
                constructTree(bestChild, this.depth);
            }// adding children for min player
            else if (!isChildMaxPlayer) {
                Node bestChild = parentNode;
                bestChild = addChildrenReturnBestChild(parentNode, allStatesAndBoardEvaluationsForGivenPieceType, isChildMaxPlayer, bestChild, Integer.MIN_VALUE);
                bestNode = bestChild;
                this.depth -= 1;
                constructTree(bestChild, this.depth);
            }
        }
    }

    private Node addChildrenReturnBestChild(Node parentNode, List<BoardStateAndEvaluationNumberTuple> allStatesAndBoardEvaluationsForGivenPieceType,
                                            boolean isChildMaxPlayer, Node bestChild, int finalEvalNo) {
        for (int j = 0; j < allStatesAndBoardEvaluationsForGivenPieceType.size(); j++) {
            // loop through states for pieces
            for (int i = 0; i < allStatesAndBoardEvaluationsForGivenPieceType.get(j).getBoardStates().size(); i++) {
                // new state with alternate color
                List<PieceAndSquareTuple> possibleBoardStates = (List<PieceAndSquareTuple>) allStatesAndBoardEvaluationsForGivenPieceType.get(j).getBoardStates().get(i);
                int lastPieceIndex = possibleBoardStates.size() - 1;
                int diceNo = Piece.getDiceFromPiece((Piece) possibleBoardStates.get(lastPieceIndex).getPiece());
                State newState = new State(
                        parentNode.getState().getBoard(), diceNo, parentNode.getState().getColor(), parentNode.getState().isCanCastleWhite(), parentNode.getState().isCanCastleBlack(),
                        parentNode.getState().isShortCastlingBlack(), parentNode.getState().isShortCastlingWhite(), parentNode.getState().isLongCastlingBlack(),
                        parentNode.getState().isShortCastlingWhite(), parentNode.getState().getCastling(), possibleBoardStates, parentNode.getState().getCumulativeTurn());
                int evalNo = (int) allStatesAndBoardEvaluationsForGivenPieceType.get(diceNo - 1).getEvaluationNumbers().get(i);
                List<PieceAndSquareTuple> prevBoardState = parentNode.getState().getPieceAndSquare();
                PieceAndSquareTuple lastPieceAndSquareTuple = possibleBoardStates.get(lastPieceIndex);
                Square destination = (Square) lastPieceAndSquareTuple.getSquare();
                Piece movingPiece = (Piece) lastPieceAndSquareTuple.getPiece();
                Square origin = null;
                for (PieceAndSquareTuple ps : prevBoardState) {
                    if (ps.getPiece() == movingPiece) {
                        Square previousSquare = (Square) ps.getSquare();
                        if (!containsSquare(possibleBoardStates, previousSquare)) {
                            origin = previousSquare; // we moved from previousSquare
                        }
                    }
                }
                // make new node with the new updated state and new destination
                Node newNode = new Node(isChildMaxPlayer, evalNo, newState, new Move(movingPiece, origin, destination, diceNo, movingPiece.getColor()));
                // add each child for each different board state for each different piece type
                // built in pruning as we only ever add better children, not all children
                // if eval number smaller
                if (isChildMaxPlayer && evalNo <= finalEvalNo) {
                    finalEvalNo = evalNo;
                    bestChild = newNode;
                // if eval number bigger
                } else if (!isChildMaxPlayer && evalNo >= finalEvalNo) {
                    finalEvalNo = evalNo;
                    bestChild = newNode;
                }
                parentNode.addChild(newNode);
            }
        }
        return bestChild;
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

    public Node findBestChild(List<Node> children, int diceRoll) {
        Node bestChild = children.get(0);
        int max = Integer.MIN_VALUE;
        for (Node child : children) {
            if (child.getBoardEvaluationNumber() > max && child.getState().getDiceRoll() == diceRoll) {
                max = child.getBoardEvaluationNumber();
                bestChild = child;
            }
        }
        return bestChild;
    }

    private void printEvaluationNumbers(List<Integer> possibleEvaluationNumbers) {
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
