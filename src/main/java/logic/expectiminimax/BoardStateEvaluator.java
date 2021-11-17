package logic.expectiminimax;

import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;

public class BoardStateEvaluator {

    public static int getBoardEvaluationNumber() {
        Game game = Game.getInstance();
        System.out.println("getBoardEvaluationNumber() " + game.toString());
        int val = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // if piece friendly
                if(game.getCurrentState().getBoardPieces()[i][j].getColor()==game.getCurrentState().color) {
                    System.out.println("color " + game.getCurrentState().color);
                    // update val for every piece
                    val+=game.getCurrentState().getBoardPieces()[i][j].getWeight();
                    // update val for favourable board position (center controL)
                    switch (game.getCurrentState().getBoardPieces()[i][j]) {
                        case BLACK_PAWN -> val += getCorrectWeights(pawnBoardWeightsW,Side.BLACK)[i][j];
                        case BLACK_KNIGHT -> val += getCorrectWeights(knightBoardWeightsW,Side.BLACK)[i][j];
                        case BLACK_BISHOP -> val += getCorrectWeights(bishopBoardWeightsW,Side.BLACK)[i][j];
                        case BLACK_ROOK -> val += getCorrectWeights(rookBoardWeightsW,Side.BLACK)[i][j];
                        case BLACK_QUEEN -> val += getCorrectWeights(queenBoardWeightsW,Side.BLACK)[i][j];
                        //// TODO figure out how to distiguish between middle and end game
                        case BLACK_KING -> val += getCorrectWeights(kingBoardWeightsMiddleGameW,Side.WHITE)[i][j];
                        case WHITE_PAWN -> val += getCorrectWeights(pawnBoardWeightsW,Side.WHITE)[i][j];
                        case WHITE_KNIGHT -> val += getCorrectWeights(knightBoardWeightsW,Side.WHITE)[i][j];
                        case WHITE_BISHOP -> val += getCorrectWeights(bishopBoardWeightsW,Side.WHITE)[i][j];
                        case WHITE_ROOK -> val += getCorrectWeights(rookBoardWeightsW,Side.WHITE)[i][j];
                        case WHITE_QUEEN -> val += getCorrectWeights(queenBoardWeightsW,Side.WHITE)[i][j];
                        //// TODO figure out how to distiguish between middle and end game
                        case WHITE_KING -> val += getCorrectWeights(kingBoardWeightsMiddleGameW,Side.WHITE)[i][j];
                    }
                }

            }
        }
        return val;
    }

    public static int getBoardEvaluationNumber(Piece[][] boardPieceState) {
        //Game game = Game.getInstance();
        //System.out.println("getBoardEvaluationNumber() " + game.toString());
        int val = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // if piece friendly
                //if(color==Side.BLACK) {
                    // update val for every piece
                    val+=boardPieceState[i][j].getWeight();
                    // update val for favourable board position (center controL)
                    switch (boardPieceState[i][j]) {
                        case BLACK_PAWN -> val += getCorrectWeights(pawnBoardWeightsW,Side.BLACK)[i][j];
                        case BLACK_KNIGHT -> val += getCorrectWeights(knightBoardWeightsW,Side.BLACK)[i][j];
                        case BLACK_BISHOP -> val += getCorrectWeights(bishopBoardWeightsW,Side.BLACK)[i][j];
                        case BLACK_ROOK -> val += getCorrectWeights(rookBoardWeightsW,Side.BLACK)[i][j];
                        case BLACK_QUEEN -> val += getCorrectWeights(queenBoardWeightsW,Side.BLACK)[i][j];
                        //// TODO figure out how to distiguish between middle and end game
                        case BLACK_KING -> val += getCorrectWeights(kingBoardWeightsMiddleGameW,Side.WHITE)[i][j];
//                        case WHITE_PAWN -> val += getCorrectWeights(pawnBoardWeightsW,Side.WHITE)[i][j];
//                        case WHITE_KNIGHT -> val += getCorrectWeights(knightBoardWeightsW,Side.WHITE)[i][j];
//                        case WHITE_BISHOP -> val += getCorrectWeights(bishopBoardWeightsW,Side.WHITE)[i][j];
//                        case WHITE_ROOK -> val += getCorrectWeights(rookBoardWeightsW,Side.WHITE)[i][j];
//                        case WHITE_QUEEN -> val += getCorrectWeights(queenBoardWeightsW,Side.WHITE)[i][j];
//                        //// TODO figure out how to distiguish between middle and end game
//                        case WHITE_KING -> val += getCorrectWeights(kingBoardWeightsMiddleGameW,Side.WHITE)[i][j];
                    }
                //}

            }
        }
        return val;
    }

    // return appropriate board configuration for correct side
    private static int[][] getCorrectWeights(int[][] weights, Side color) {
        System.out.println("board state eval get correct weights");
        if (color == Side.WHITE) {
            return weights;
        }
        return(flipMatrixHorizontal(weights));
    }

    // flips the weight matrix horizontally so if can be used for either black or white
    private static int[][] flipMatrixHorizontal(int[][] matrix) {
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

    private static int[][] knightBoardWeightsW = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50} };
    private static int[][] queenBoardWeightsW = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20} };
    private static int[][] bishopBoardWeightsW = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20} };
    private static int[][] pawnBoardWeightsW = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}};
    private static int[][] rookBoardWeightsW = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {0,  0,  0,  5,  5,  0,  0,  0} };
    private static int[][] kingBoardWeightsMiddleGameW = {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {20, 30, 10,  0,  0, 10, 30, 20} };
    private static int[][] kingBoardWeightsEndGameW = {
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50} };

}
