package logic.algorithms;

import logic.LegalMoveGenerator;
import logic.ML.OriginAndDestSquare;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.ArrayList;
import java.util.List;

public class BoardStateEvaluator {

    public static int getBoardEvaluationNumber(State state, Side side) { // for ML
        int evalNo = 0;

        ArrayList<OriginAndDestSquare> originAndDestSquares = LegalMoveGenerator.getAllLegalMoves(state, side);

        for (OriginAndDestSquare tempMove : originAndDestSquares) {
            Square s = (Square) tempMove.getOrigin();
            Piece p = state.getBoard().getPieceAt(s);

            if (p.getColor() == side) {
                evalNo += p.getWeight();
                if(p==Piece.BLACK_PAWN || p==Piece.WHITE_PAWN) {
                    evalNo += getCorrectWeights(pawnBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_KNIGHT || p==Piece.WHITE_KNIGHT) {
                    evalNo += getCorrectWeights(knightBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_BISHOP || p==Piece.WHITE_BISHOP) {
                    evalNo += getCorrectWeights(bishopBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_ROOK || p==Piece.WHITE_ROOK) {
                    evalNo += getCorrectWeights(rookBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_QUEEN || p==Piece.WHITE_QUEEN) {
                    evalNo += getCorrectWeights(queenBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_KING || p==Piece.WHITE_KING) { // TODO, implement turn number to QL
                    evalNo += getCorrectWeights(kingBoardWeightsMiddleGameW, side)[s.getRank()-1][s.getFile()];
                }
            } else {
                evalNo -= p.getWeight();
                if(p==Piece.BLACK_PAWN || p==Piece.WHITE_PAWN) {
                    evalNo -= getCorrectWeights(pawnBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_KNIGHT || p==Piece.WHITE_KNIGHT) {
                    evalNo -= getCorrectWeights(knightBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_BISHOP || p==Piece.WHITE_BISHOP) {
                    evalNo -= getCorrectWeights(bishopBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_ROOK || p==Piece.WHITE_ROOK) {
                    evalNo -= getCorrectWeights(rookBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_QUEEN || p==Piece.WHITE_QUEEN) {
                    evalNo -= getCorrectWeights(queenBoardWeightsW, side)[s.getRank()-1][s.getFile()];
                } else if (p==Piece.BLACK_KING || p==Piece.WHITE_KING) {
                    evalNo -= getCorrectWeights(kingBoardWeightsMiddleGameW, side)[s.getRank()-1][s.getFile()];
                }
            }
        }
        return evalNo;
    }


    // return a value of a board for a given side taking into account the piece values and their corresponding board square position values
    public static int getBoardEvaluationNumber(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int turn) {
        int evalNo = 0;
        for (PieceAndSquareTuple ps : nodePieceAndSquare) {
            Piece p = (Piece) ps.getPiece();
            Square s = (Square) ps.getSquare();
            // if piece friendly add weight, else subtract opposite weight
            if (p.getColor()==color) {
                evalNo += p.getWeight();
                evalNo += getCorrectWeights(p,turn)[s.getRank()-1][s.getFile()];
            } else {
                evalNo -= p.getWeight();
                evalNo -= getCorrectWeights(p,turn)[s.getRank()-1][s.getFile()];
            }
        }
        return evalNo;
    }

    // return appropriate board configuration for correct side
    private static int[][] getCorrectWeights(Piece piece,int turn) {
        if (piece==Piece.WHITE_KING && turn<50) {return kingBoardWeightsMiddleGameB ;}
        if (piece==Piece.BLACK_KING && turn<50) {return kingBoardWeightsMiddleGameW;}
        if (piece==Piece.WHITE_KING && turn>=50) {return kingBoardWeightsEndGameB;}
        if (piece==Piece.BLACK_KING && turn>=50) {return kingBoardWeightsEndGameW;}
        return switch (piece) {
            case WHITE_PAWN -> pawnBoardWeightsB;
            case BLACK_PAWN -> pawnBoardWeightsW;
            case WHITE_KNIGHT -> knightBoardWeightsB;
            case BLACK_KNIGHT -> knightBoardWeightsW;
            case WHITE_BISHOP -> bishopBoardWeightsB;
            case BLACK_BISHOP -> bishopBoardWeightsW;
            case WHITE_ROOK -> rookBoardWeightsB;
            case BLACK_ROOK -> rookBoardWeightsW;
            case WHITE_QUEEN -> queenBoardWeightsB;
            case BLACK_QUEEN -> queenBoardWeightsW;
            default -> null;
        };
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

    private static int[][] knightBoardWeightsB = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50} };
    private static int[][] knightBoardWeightsW = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50} };

    private static int[][] queenBoardWeightsB = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20} };
    private static int[][] queenBoardWeightsW = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20} };
    
    private static int[][] bishopBoardWeightsB = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20} };
    private static int[][] bishopBoardWeightsW = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20} };
    
    private static int[][] pawnBoardWeightsB = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {0,  0,  0,  0,  0,  0,  0,  0} };
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
    private static int[][] rookBoardWeightsB = {
            {0,  0,  0,  5,  5,  0,  0,  0},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {5, 10, 10, 10, 10, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0} };
            
    private static int[][] kingBoardWeightsMiddleGameW = {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {20, 30, 10,  0,  0, 10, 30, 20} };
    private static int[][] kingBoardWeightsMiddleGameB = {
            {20, 30, 10,  0,  0, 10, 30, 20},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30}, };
    
    private static int[][] kingBoardWeightsEndGameW = {
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50} };
    private static int[][] kingBoardWeightsEndGameB = {
            {-50,-30,-30,-30,-30,-30,-30,-50},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-50,-40,-30,-20,-20,-30,-40,-50} };

}
