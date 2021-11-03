package logic.player;
import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.expectiminimax.Node;
import java.util.List;

import static logic.enums.Side.WHITE;

public class ExpectiMiniMaxPlayer extends AIPlayer{

    //Node node = new Node(0);
    private final boolean DEBUG = true;

    public ExpectiMiniMaxPlayer(Side color) {
        super(color);
    }

    @Override
    public Move chooseMove(State state) {
        List<Move> validMoves = getValidMoves(state);
        int[][] knightBoardWeightsW = {
                {1,2,3,3,3,3,2,1},
                {2,3,4,4,4,4,3,2},
                {3,4,4,4,4,4,4,3},
                {3,4,4,4,4,4,4,3},
                {3,4,4,4,4,4,4,3},
                {3,4,4,4,4,4,4,3},
                {2,3,4,4,4,4,3,2},
                {1,2,3,3,3,3,2,1} };
        int[][] kingBoardWeightsW = {
                {4,5,3,3,3,3,5,4},
                {4,4,3,3,3,3,4,4},
                {4,3,3,3,3,3,3,4},
                {4,3,3,2,2,3,3,4},
                {4,2,2,1,1,2,2,4},
                {4,2,2,1,1,2,2,4},
                {3,2,2,1,1,2,2,3},
                {3,2,2,1,1,2,2,3} };
        int[][] queenBoardWeightsW = {
                {1,2,2,3,3,2,2,1},
                {2,3,3,3,3,3,3,2},
                {2,3,4,4,4,4,3,2},
                {3,3,4,4,4,4,3,3},
                {3,3,4,4,4,4,3,3},
                {2,3,4,4,4,4,3,2},
                {2,3,3,3,3,3,3,2},
                {1,2,2,3,3,2,2,1}};
        int[][] bishopBoardWeightsW = {
                {3,2,2,2,2,2,2,3},
                {2,3,2,2,2,2,3,2},
                {2,3,3,3,3,3,3,2},
                {2,3,3,4,4,3,3,2},
                {2,4,3,4,4,3,4,2},
                {2,3,3,4,4,3,3,2},
                {2,3,3,3,3,3,3,2},
                {1,2,2,2,2,2,2,1} };
        int[][] pawnBoardWeightsW = {
                {2,2,2,2,2,2,2,2},
                {2,2,2,1,1,2,2,2},
                {2,1,1,2,2,1,1,2},
                {2,2,2,3,3,2,2,2},
                {2,2,2,3,3,2,2,2},
                {2,2,3,4,4,3,2,2},
                {5,5,5,5,5,5,5,5},
                {2,2,2,2,2,2,2,2} };
        int[][] rookBoardWeightsW = {
                {2,2,2,3,3,2,2,2},
                {1,2,2,2,2,2,2,1},
                {1,2,2,2,2,2,2,1},
                {1,2,2,2,2,2,2,1},
                {1,2,2,2,2,2,2,1},
                {1,2,2,2,2,2,2,1},
                {4,5,5,5,5,5,5,4},
                {3,3,3,3,3,3,3,3} };

        System.out.println("valid moves: " + validMoves.toString());


        for (int i = 0; i < validMoves.size(); i++) {
            // if target piece not friendly and target destination is not empty then capture always (not sure if we need the empty condition)
            if(state.getBoard().getPieceAt(validMoves.get(i).getDestination()).isFriendly(WHITE) && state.getBoard().getPieceAt(validMoves.get(i).getDestination()) != Piece.EMPTY) {
                if (DEBUG) {
                    System.out.println("Dest: " + validMoves.get(i).getDestination().toString());
                    System.out.println("Piece at Dest: " + state.getBoard().getPieceAt(validMoves.get(i).getDestination()).toString());
                    System.out.println("piece as dest is friendly: " + state.getBoard().getPieceAt(validMoves.get(i).getDestination()).isFriendly(WHITE));
                    System.out.println("valid move to choose: " + validMoves.get(i));
                    System.out.println("CAPTURE MOVE: ");
                }
                return validMoves.get(i);
            }
        }
        // checks what piece AI got on dice roll
        switch (validMoves.get(0).getPiece().getType()) {
            // all cases activated if piece has to move to empty square
            case PAWN -> {
                if (DEBUG) {System.out.println("PAWN MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, flipMatrixHorizontal(pawnBoardWeightsW));
                // gets max value of most favorable move position
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                return validMoves.get(favourableMoveMaxIndex);
            }
            case KNIGHT -> {
                if (DEBUG) {System.out.println("KNIGHT MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, flipMatrixHorizontal(knightBoardWeightsW));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                return validMoves.get(favourableMoveMaxIndex);
            }
            case BISHOP -> {
                if (DEBUG) {System.out.println("BISHOP MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, flipMatrixHorizontal(bishopBoardWeightsW));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                return validMoves.get(favourableMoveMaxIndex);
            }
            case ROOK -> {
                if (DEBUG) {System.out.println("ROOK MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, flipMatrixHorizontal(rookBoardWeightsW));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                return validMoves.get(favourableMoveMaxIndex);
            }
            case QUEEN -> {
                if (DEBUG) {System.out.println("QUEEN MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, flipMatrixHorizontal(queenBoardWeightsW));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                return validMoves.get(favourableMoveMaxIndex);
            }
            case KING -> {
                if (DEBUG) {System.out.println("KING MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, flipMatrixHorizontal(kingBoardWeightsW));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                return validMoves.get(favourableMoveMaxIndex);
            }
        }
        return validMoves.get(0);
    }

    // flips the weight matrix horizontally so if can be used for either black or white
    private int[][] flipMatrixHorizontal(int[][] matrix) {
        int temp = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length / 2; j++) {
                temp = matrix[i][j];
                matrix[i][j] = matrix[i][matrix.length - 1 - j];
                matrix[i][matrix.length - 1 -j] = temp;
            }
        }
        return matrix;
    }


    // gets the weights of valid moves
    private int[] updateBoardWeights(State state, int[][] boardWeights) {
        List<Move> validMoves = getValidMoves(state);
        int[] weightsOfValidMoves = new int[validMoves.size()];
        for (int i = 0; i < validMoves.size(); i++) {
            //get rank is 1 indexed, so subtracted 1 to make it 0 indexed
            weightsOfValidMoves[i]=boardWeights[validMoves.get(i).getDestination().getRank()-1][validMoves.get(i).getDestination().getFile()];
            if (DEBUG) {System.out.println("weights of valid moves: " + weightsOfValidMoves[i] + " valid move: " + validMoves.get(i));}
        }
        return weightsOfValidMoves;
    }

    // gets index of max value in int[]
    private int maxValueAt(int[] arr) {
        int max = arr[0];
        int pos = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                pos = i;
            }
        }
        return pos;
    }

    // TODO implement expectiminimax
    // unused
    public float expectiminimax(Node node, boolean is_max) {
        if ( node.getPAWN() == null && node.getKNIGHT() == null && node.getBISHOP()==null
                && node.getROOK()==null && node.getQUEEN()==null && node.getKING()==null ) {
            return node.getValue();
        }
        if (is_max) {
            return Math.max(
                    Math.max(Math.max(expectiminimax(node.getPAWN(), false), expectiminimax(node.getKNIGHT(), false)),
                    Math.max(expectiminimax(node.getBISHOP(), false),expectiminimax(node.getROOK(), false))),
                    Math.max(expectiminimax(node.getQUEEN(), false),expectiminimax(node.getKING(), false))
            );
        }
        else {
            return (float) ((
                    expectiminimax(node.getPAWN(), true) + expectiminimax(node.getKNIGHT(), true) + expectiminimax(node.getKNIGHT(), true) +
                            expectiminimax(node.getROOK(), true) + expectiminimax(node.getQUEEN(), true) + expectiminimax(node.getKING(), true)
                    )
                    / 6.0);
        }
    }

}
