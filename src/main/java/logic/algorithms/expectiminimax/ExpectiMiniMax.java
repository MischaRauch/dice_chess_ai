package logic.algorithms.expectiminimax;

import logic.BoardStateAndEvaluationNumberTuple;
import logic.Move;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.algorithms.BoardStateGenerator;
import logic.enums.Piece;

import java.util.ArrayList;
import java.util.List;

public class ExpectiMiniMax {

    private final BoardStateGenerator gen = new BoardStateGenerator();
    private ExpectiMiniMaxTree tree;
    private ExpectiMiniMaxNode bestNode;
    private boolean isHybrid;

    public void constructTree(int depth, State initialBoardState, boolean isHybrid) {
        this.tree = new ExpectiMiniMaxTree();
        tree.setRoot(new ExpectiMiniMaxNode(true, initialBoardState));
        constructTree(tree.getRoot(), depth, isHybrid);
        this.isHybrid = isHybrid; // put the new parameter to here
    }

    private void constructTree(ExpectiMiniMaxNode parentNode, int depth, boolean isHybrid) {
        this.isHybrid = isHybrid;
        if (depth == 0) {
            bestNode = parentNode;
        }
        while (depth != 0) {
            List<BoardStateAndEvaluationNumberTuple> allStatesAndBoardEvaluationsForGivenPieceType = generateAllPossibleStatesForGivenNode(parentNode);
            List<Piece> pieceList = generatePieceTypesForGivenNode(parentNode);

            boolean isChildMaxPlayer = !parentNode.isMaxPlayer();
            // parent (current node minimizer -> child maximizer
            if (!isChildMaxPlayer) {
                ExpectiMiniMaxNode bestChild = addChildrenReturnBestChild(parentNode, allStatesAndBoardEvaluationsForGivenPieceType, !isChildMaxPlayer, pieceList);
                depth -= 1;
                constructTree(bestChild, depth, isHybrid);
            }
            // parent (current node) maximizer -> child minimizer
            else if (isChildMaxPlayer) {
                ExpectiMiniMaxNode bestChild = addChildrenReturnBestChild(parentNode, allStatesAndBoardEvaluationsForGivenPieceType, isChildMaxPlayer, pieceList);
                depth -= 1;
                constructTree(bestChild, depth, isHybrid);
            }
        }
    }

    private List<Piece> generatePieceTypesForGivenNode(ExpectiMiniMaxNode parentNode) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            // evaluation numbers for i-th dice roll
            List<Integer> possibleEvaluationNumbersForGivenPiece = gen.getPossibleBoardStatesWeightsOfSpecificPiece(parentNode.getPreviousState().getPieceAndSquare(),
                    parentNode.getPreviousState().getColor(), i, parentNode.getPreviousState(), isHybrid);
            if (!possibleEvaluationNumbersForGivenPiece.isEmpty()) {
                pieces.add(Piece.getPieceFromDice(i, parentNode.getPreviousState().getColor()));
            }
        }
        return pieces;
    }

    private List<BoardStateAndEvaluationNumberTuple> generateAllPossibleStatesForGivenNode(ExpectiMiniMaxNode parentNode) {
        List<BoardStateAndEvaluationNumberTuple> allStatesAndBoardEvaluationsForGivenPieceType = new ArrayList<>();
        // state generation
        // loop through for all 6 dice numbers and generate all possible states
        for (int i = 1; i < 7; i++) {
            // evaluation numbers for i-th dice roll
            List<Integer> possibleEvaluationNumbersForGivenPiece = gen.getPossibleBoardStatesWeightsOfSpecificPiece(parentNode.getPreviousState().getPieceAndSquare(),
                    parentNode.getPreviousState().getColor(), i, parentNode.getPreviousState(), isHybrid);
            // possible board states for i-th dice roll
            List<List<PieceAndSquareTuple>> possibleBoardStatesForGivenPiece = gen.getPossibleBoardStates(parentNode.getPreviousState().getPieceAndSquare(),
                    parentNode.getPreviousState().getColor(), i, parentNode.getPreviousState());
            // add this tuple of list of possible eval numbers and board states to tuple list
            // only add if not empty
            //System.out.println(possibleEvaluationNumbersForGivenPiece);
            if (!possibleEvaluationNumbersForGivenPiece.isEmpty()) {
                allStatesAndBoardEvaluationsForGivenPieceType.add(new BoardStateAndEvaluationNumberTuple(possibleBoardStatesForGivenPiece, possibleEvaluationNumbersForGivenPiece));
            }
        }
        return allStatesAndBoardEvaluationsForGivenPieceType;
    }


    private ExpectiMiniMaxNode addChildrenReturnBestChild(ExpectiMiniMaxNode parentNode, List<BoardStateAndEvaluationNumberTuple> allStatesAndBoardEvaluationsForGivenPieceType,
                                                          boolean isChildMaxPlayer, List<Piece> pieceList) {

        int chanceDivider = allStatesAndBoardEvaluationsForGivenPieceType.size();
        for (int i = 0; i < allStatesAndBoardEvaluationsForGivenPieceType.size(); i++) {
            // only add for dice numbers that are not empty

            List<List<PieceAndSquareTuple>> statesForGivenPiece = allStatesAndBoardEvaluationsForGivenPieceType.get(i).getBoardStates();

            List<Integer> boardEvaluationNumbersForGivenPiece = allStatesAndBoardEvaluationsForGivenPieceType.get(i).getEvaluationNumbers();

            int sumOfEvalNumbers = getSumOfEvalNumbers(allStatesAndBoardEvaluationsForGivenPieceType, i);
            int nodeValue = sumOfEvalNumbers / chanceDivider;
            int bestBoardEvaluationIndex = getBestBoardEvaluationNumberIndex(allStatesAndBoardEvaluationsForGivenPieceType, i, isChildMaxPlayer);
            //System.out.println(bestBoardEvaluationIndex);
            //System.out.println(statesForGivenPiece.size());
            List<PieceAndSquareTuple> bestState = statesForGivenPiece.get(bestBoardEvaluationIndex);

            Piece bestStatePieceThatMoved = (Piece) bestState.get(bestState.size() - 1).getPiece();

            List<Move> legalMovesForGivePiece = gen.getValidMovesForGivenPiece(parentNode.getPreviousState(), pieceList.get(i));

            State newState = new State(parentNode.getPreviousState().getBoard(), Piece.getDiceFromPiece(bestStatePieceThatMoved),
                    bestStatePieceThatMoved.getColor(), parentNode.getPreviousState().isCanCastleWhite(), parentNode.getPreviousState().isCanCastleBlack(), parentNode.getPreviousState().isShortCastlingBlack(),
                    parentNode.getPreviousState().isShortCastlingWhite(), parentNode.getPreviousState().isLongCastlingBlack(), parentNode.getPreviousState().isLongCastlingWhite(),
                    parentNode.getPreviousState().castling, parentNode.getPreviousState().getPieceAndSquare(), parentNode.getPreviousState().getCumulativeTurn());

            ExpectiMiniMaxNode newNode = new ExpectiMiniMaxNode(isChildMaxPlayer, statesForGivenPiece, boardEvaluationNumbersForGivenPiece,
                    chanceDivider, nodeValue, newState, legalMovesForGivePiece, pieceList.get(i));

            parentNode.addChild(newNode);
        }
        return parentNode.getChildren().get(getBestChildIndex(parentNode, !isChildMaxPlayer));
    }

    // sum evaluation numbers for given piece type
    private int getSumOfEvalNumbers(List<BoardStateAndEvaluationNumberTuple> allStatesAndBoardEvaluationsForGivenPieceType, int indexZeroBased) {
        int sum = 0;
        for (int i = 0; i < allStatesAndBoardEvaluationsForGivenPieceType.size(); i++) {
            for (int j = 0; j < allStatesAndBoardEvaluationsForGivenPieceType.get(indexZeroBased).getEvaluationNumbers().size(); j++) {
                sum += (int) allStatesAndBoardEvaluationsForGivenPieceType.get(indexZeroBased).getEvaluationNumbers().get(j);
            }
        }
        return sum;
    }

    // return values between 0 and 5
    private int getBestChildIndex(ExpectiMiniMaxNode parentNode, boolean isMax) {
        int max = Integer.MAX_VALUE;
        int min = Integer.MIN_VALUE;
        int indexBestChild = -1;
        int index = 0;
        for (ExpectiMiniMaxNode n : parentNode.getChildren()) {
            if (!isMax && n.getNodeValue() > min) {
                min = n.getNodeValue();
                indexBestChild = index;
                index++;
            } else if (isMax && n.getNodeValue() < max) {
                max = n.getNodeValue();
                indexBestChild = index;
                index++;
            }
        }
        return indexBestChild;
    }

    private int getBestBoardEvaluationNumberIndex(List<BoardStateAndEvaluationNumberTuple> allStatesAndBoardEvaluationsForGivenPieceType, int indexZeroBased, boolean isMax) {
        int bestBoardEvaluationNumber = 0;
        int bestIndex = 0;
        int max = Integer.MAX_VALUE;
        int min = Integer.MIN_VALUE;
        for (int i = 0; i < allStatesAndBoardEvaluationsForGivenPieceType.size(); i++) {
            for (int j = 0; j < allStatesAndBoardEvaluationsForGivenPieceType.get(indexZeroBased).getEvaluationNumbers().size(); j++) {
                // mazimizer
                if (isMax) {
                    if ((int) allStatesAndBoardEvaluationsForGivenPieceType.get(indexZeroBased).getEvaluationNumbers().get(j) > min) {
                        min = (int) allStatesAndBoardEvaluationsForGivenPieceType.get(indexZeroBased).getEvaluationNumbers().get(j);
                        bestBoardEvaluationNumber = min;
                        bestIndex = j;
                    }
                    // minimizer
                } else {
                    if ((int) allStatesAndBoardEvaluationsForGivenPieceType.get(indexZeroBased).getEvaluationNumbers().get(j) < max) {
                        max = (int) allStatesAndBoardEvaluationsForGivenPieceType.get(indexZeroBased).getEvaluationNumbers().get(j);
                        bestBoardEvaluationNumber = max;
                        bestIndex = j;
                    }
                }
            }
        }
        return bestIndex;
    }

    // only called after tree generated
    private int getBestBoardEvaluationIndexFromBestNode() {
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        int bestEval = 0;
        int indexForBestEval = -1;

        // get index of the best evaluation number -> will correspond to index of best move
        for (Integer i : bestNode.getBoardEvaluationNumbersForGivenPiece()) {
            if (bestNode.isMaxPlayer() && i > min) {
                min = i;
                bestEval = min;
            } else if (!bestNode.isMaxPlayer() && i < max) {
                max = i;
                bestEval = max;
            }
        }
        for (int i = 0; i < bestNode.getBoardEvaluationNumbersForGivenPiece().size(); i++) {
            if (bestEval == bestNode.getBoardEvaluationNumbersForGivenPiece().get(i)) {
                indexForBestEval = i;
                break;
            }
        }
        return indexForBestEval;
    }

    public Move getBestMoveForBestNode() {
        List<Move> allMoveForGivenPiece = gen.getValidMovesForGivenPiece(bestNode.getPreviousState(), bestNode.getPiece());
        List<PieceAndSquareTuple> bestStatePAS = bestNode.getPossibleBoardStatesForGivenPiece().get(getBestBoardEvaluationIndexFromBestNode());
        for (PieceAndSquareTuple t : bestStatePAS) {
            Piece coloredPiece = (Piece) t.getPiece();
            for (Move m : allMoveForGivenPiece) {
                if (t.getSquare() == m.getDestination() && t.getPiece() == m.getPiece() && bestNode.getPiece().getColor() == coloredPiece.getColor()) {
                    return m;
                }
            }
        }
        return null;
    }

    public ExpectiMiniMaxTree getTree() {
        return tree;
    }

}
