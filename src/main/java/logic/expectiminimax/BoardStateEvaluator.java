package logic.expectiminimax;

import logic.PieceAndSquareTuple;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardStateEvaluator {

    public static int getBoardEvaluationNumberOfSpecificPiece(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int diceRoll) {
        //Map<Piece,Integer> map = new HashMap<>();
        int val = 0;
        // loop through each piece and square tuple in entire board state
        for (PieceAndSquareTuple t : nodePieceAndSquare) {
            // if the piece in the board is the color and piece type as our dice roll e.i. black knight
            if (t.getPiece()==Piece.getPieceFromDice(diceRoll, color)) {
                Piece piece = (Piece) t.getPiece();
                // update it's eval number using weight from piece
                val += piece.getWeight();
                Square s = (Square) t.getSquare();
                // update it's weight due to being on favourable location on board
                switch (piece) {
                    case BLACK_PAWN -> val += getCorrectWeights(pawnBoardWeightsW, Side.BLACK)[s.getRank()-1][s.getFile()];
                    case BLACK_KNIGHT -> val += getCorrectWeights(knightBoardWeightsW, Side.BLACK)[s.getRank()-1][s.getFile()];
                    case BLACK_BISHOP -> val += getCorrectWeights(bishopBoardWeightsW, Side.BLACK)[s.getRank()-1][s.getFile()];
                    case BLACK_ROOK -> val += getCorrectWeights(rookBoardWeightsW, Side.BLACK)[s.getRank()-1][s.getFile()];
                    case BLACK_QUEEN -> val += getCorrectWeights(queenBoardWeightsW, Side.BLACK)[s.getRank()-1][s.getFile()];
                    //// TODO figure out how to distiguish between middle and end game
                    case BLACK_KING -> val += getCorrectWeights(kingBoardWeightsMiddleGameW, Side.WHITE)[s.getRank()-1][s.getFile()];
                    case WHITE_PAWN -> val += getCorrectWeights(pawnBoardWeightsW, Side.WHITE)[s.getRank()-1][s.getFile()];
                    case WHITE_KNIGHT -> val += getCorrectWeights(knightBoardWeightsW, Side.WHITE)[s.getRank()-1][s.getFile()];
                    case WHITE_BISHOP -> val += getCorrectWeights(bishopBoardWeightsW, Side.WHITE)[s.getRank()-1][s.getFile()];
                    case WHITE_ROOK -> val += getCorrectWeights(rookBoardWeightsW, Side.WHITE)[s.getRank()-1][s.getFile()];
                    case WHITE_QUEEN -> val += getCorrectWeights(queenBoardWeightsW, Side.WHITE)[s.getRank()-1][s.getFile()];
                    //// TODO figure out how to distiguish between middle and end game
                    case WHITE_KING -> val += getCorrectWeights(kingBoardWeightsMiddleGameW, Side.WHITE)[s.getRank()-1][s.getFile()];
                }
            }
        }
        System.out.println("final val" + val);
        return val;
    }

    public static int getBoardEvaluationNumber(List<PieceAndSquareTuple> nodePieceAndSquare, Side color) {
        int evalNo = 0;
        for (PieceAndSquareTuple ps : nodePieceAndSquare) {
            Piece p = (Piece) ps.getPiece();
            Square s = (Square) ps.getSquare();
            // if piece friendly add weight, else subtract opposite weight
            if (p.getColor()==color) {
                evalNo += p.getWeight();
                if(p==Piece.BLACK_PAWN || p==Piece.WHITE_PAWN) {
                    evalNo += getCorrectWeights(pawnBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_KNIGHT || p==Piece.WHITE_KNIGHT) {
                    evalNo += getCorrectWeights(knightBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_BISHOP || p==Piece.WHITE_BISHOP) {
                    evalNo += getCorrectWeights(bishopBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_ROOK || p==Piece.WHITE_ROOK) {
                    evalNo += getCorrectWeights(rookBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_QUEEN || p==Piece.WHITE_QUEEN) {
                    evalNo += getCorrectWeights(queenBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_KING || p==Piece.WHITE_KING) {
                    evalNo += getCorrectWeights(kingBoardWeightsMiddleGameW,color)[s.getRank()-1][s.getFile()];
                } //TODO king end game weights
            } else {
                evalNo -= p.getWeight();
                if(p==Piece.BLACK_PAWN || p==Piece.WHITE_PAWN) {
                    evalNo -= getCorrectWeights(pawnBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_KNIGHT || p==Piece.WHITE_KNIGHT) {
                    evalNo -= getCorrectWeights(knightBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_BISHOP || p==Piece.WHITE_BISHOP) {
                    evalNo -= getCorrectWeights(bishopBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_ROOK || p==Piece.WHITE_ROOK) {
                    evalNo -= getCorrectWeights(rookBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_QUEEN || p==Piece.WHITE_QUEEN) {
                    evalNo -= getCorrectWeights(queenBoardWeightsW,color)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_KING || p==Piece.WHITE_KING) {
                    evalNo -= getCorrectWeights(kingBoardWeightsMiddleGameW,color)[s.getRank()-1][s.getFile()];
                } //TODO king end game weights
            }
        }
        return evalNo;
    }

    // return appropriate board configuration for correct side
    private static int[][] getCorrectWeights(int[][] weights, Side color) {
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
