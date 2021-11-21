package logic.player;

import logic.PieceAndSquareTuple;
import logic.enums.Piece;
import logic.enums.Side;
import logic.Move;
import logic.State;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class BasicAIPlayer extends AIPlayer {

    private final boolean DEBUG = false;
    private final boolean DEBUG2 = false;

    public BasicAIPlayer(Side color) {
        super(color);
    }

    @Override
    public Move chooseMove(State state) {
        List<Move> validMoves = getValidMoves(state);
        // heavily inspired by https://www.chessprogramming.org/Simplified_Evaluation_Function
        if (DEBUG) {System.out.println("valid moves: " + validMoves.toString());}
        // capture
        List<PieceAndSquareTuple> pieceAndSquare = (List<PieceAndSquareTuple>) state.getPieceAndSquare().stream().collect(Collectors.toList());
        List<PieceAndSquareTuple> pieceAndSquare2 = (List<PieceAndSquareTuple>) state.getPieceAndSquare().stream().collect(Collectors.toList());
        for (int i = 0; i < validMoves.size(); i++) {
            // if target piece not friendly and target destination is not empty then capture always (not sure if we need the empty condition)
            if(!state.getBoard().getPieceAt(validMoves.get(i).getDestination()).isFriendly(color) && state.getBoard().getPieceAt(validMoves.get(i).getDestination()) != Piece.EMPTY) {
                if (DEBUG) {
                    System.out.println("Dest: " + validMoves.get(i).getDestination().toString());
                    System.out.println("Piece at Dest: " + state.getBoard().getPieceAt(validMoves.get(i).getDestination()).toString());
                    System.out.println("piece as dest is friendly: " + !state.getBoard().getPieceAt(validMoves.get(i).getDestination()).isFriendly(color));
                    System.out.println("valid move to choose: " + validMoves.get(i));
                    System.out.println("CAPTURE MOVE: ");
                }
                //// TODO not sure if we should update board pieces before or after updating state
                updatePieceAndSquareState(state,validMoves.get(i));
                state.printPieceAndSquare();
                // return first set of legal moves
                return validMoves.get(i);
            }
        }
        // checks what piece AI got on dice roll
        switch (validMoves.get(0).getPiece().getType()) {
            // all cases activated if piece has to move to empty square
            case PAWN -> {
                if (DEBUG) {System.out.println("PAWN MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, getCorrectWeights(pawnBoardWeightsW,color));
                // gets max value of most favorable move position
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                updatePieceAndSquareState(state,validMoves.get(favourableMoveMaxIndex));
                state.printPieceAndSquare();
                return validMoves.get(favourableMoveMaxIndex);
            }
            case KNIGHT -> {
                if (DEBUG) {System.out.println("KNIGHT MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, getCorrectWeights(knightBoardWeightsW,color));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                updatePieceAndSquareState(state,validMoves.get(favourableMoveMaxIndex));
                state.printPieceAndSquare();
                return validMoves.get(favourableMoveMaxIndex);
            }
            case BISHOP -> {
                if (DEBUG) {System.out.println("BISHOP MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, getCorrectWeights(bishopBoardWeightsW,color));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                updatePieceAndSquareState(state,validMoves.get(favourableMoveMaxIndex));
                state.printPieceAndSquare();
                return validMoves.get(favourableMoveMaxIndex);
            }
            case ROOK -> {
                if (DEBUG) {System.out.println("ROOK MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, getCorrectWeights(rookBoardWeightsW,color));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                updatePieceAndSquareState(state,validMoves.get(favourableMoveMaxIndex));
                state.printPieceAndSquare();
                return validMoves.get(favourableMoveMaxIndex);
            }
            case QUEEN -> {
                if (DEBUG) {System.out.println("QUEEN MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, getCorrectWeights(queenBoardWeightsW,color));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                updatePieceAndSquareState(state,validMoves.get(favourableMoveMaxIndex));
                state.printPieceAndSquare();
                return validMoves.get(favourableMoveMaxIndex);
            }
            case KING -> {
                /// TODO add end logic.game condition; but need to know the turn somehow. we don't know turn as we have no access to logic.game
                if (DEBUG) {System.out.println("KING MAX MOVE");}
                int[] weightsOfValidMoves = updateBoardWeights(state, getCorrectWeights(kingBoardWeightsMiddleGameW,color));
                int favourableMoveMaxIndex = maxValueAt(weightsOfValidMoves);
                updatePieceAndSquareState(state,validMoves.get(favourableMoveMaxIndex));
                state.printPieceAndSquare();
                return validMoves.get(favourableMoveMaxIndex);
            }
        }
        // optimize later, rn takes first index
        Move chosenMove = validMoves.get(0);
        return chosenMove;
    }


    private void updatePieceAndSquareState(State state, Move move) {
        state.printPieceAndSquare();

        List<PieceAndSquareTuple> list = new CopyOnWriteArrayList<PieceAndSquareTuple>();
        ListIterator litr = state.getPieceAndSquare().listIterator();

        while(litr.hasNext()) {
            PieceAndSquareTuple t = (PieceAndSquareTuple) litr.next();
            if (t.getSquare() == move.getOrigin()) {
                if( DEBUG2)System.out.println("skipped: " + t.getPiece().toString() + t.getSquare().toString());
            } else {
                list.add((PieceAndSquareTuple)t);
                if( DEBUG2)System.out.println("added: " + t.getPiece().toString() + t.getSquare().toString());
            }
        }
        PieceAndSquareTuple tupleForLeavingSquare = new PieceAndSquareTuple(move.getPiece(), move.getDestination());
        list.add(tupleForLeavingSquare);
        if( DEBUG2)System.out.println("added:2 " + tupleForLeavingSquare.getPiece().toString() + tupleForLeavingSquare.getSquare().toString());

        List<PieceAndSquareTuple> list2 = new CopyOnWriteArrayList<PieceAndSquareTuple>();

        ListIterator litr2 = list.listIterator();
        while(litr2.hasNext()) {
            PieceAndSquareTuple t = (PieceAndSquareTuple) litr2.next();
            if (tupleForLeavingSquare.getSquare()==t.getSquare() &&  litr2.nextIndex()==list.size()-1) {
                litr2.next();
                if( DEBUG2)System.out.println("skipped: " + t.getPiece().toString() + t.getSquare().toString());
            } else if (tupleForLeavingSquare.getSquare()!=t.getSquare() && litr2.nextIndex()!=list.size()-1){
                list2.add(t);
                if( DEBUG2)System.out.println("added:1 " + t.getPiece().toString() + t.getSquare().toString());
            } else if (litr2.nextIndex()==list.size()-1){
                list2.add(t);
                if( DEBUG2)System.out.println("added:2 " + t.getPiece().toString() + t.getSquare().toString());
            }
        }
        list2.add(tupleForLeavingSquare);

        state.setPieceAndSquare(list2);
    }

    private int[][] getCorrectWeights(int[][] weights, Side color) {
        if (color == Side.WHITE) {
            return weights;
        }
        if (DEBUG) {System.out.println("Side is black from getCorrectweights");}
        return(flipMatrixHorizontal(weights));
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
            weightsOfValidMoves[i] = boardWeights[validMoves.get(i).getDestination().getRank()-1][validMoves.get(i).getDestination().getFile()];
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

    private int[][] knightBoardWeightsW = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50} };
    private int[][] queenBoardWeightsW = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20} };
    private int[][] bishopBoardWeightsW = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20} };
    private int[][] pawnBoardWeightsW = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}};
    private int[][] rookBoardWeightsW = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {0,  0,  0,  5,  5,  0,  0,  0} };
    private int[][] kingBoardWeightsMiddleGameW = {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {20, 30, 10,  0,  0, 10, 30, 20} };
    private int[][] kingBoardWeightsEndGameW = {
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50} };
}

