package logic.algorithms;

import logic.*;
import logic.ML.DQL;
import logic.ML.OriginAndDestSquare;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;

import java.util.ArrayList;
import java.util.List;

public class BoardStateEvaluator {

    public static int getBoardEvaluationNumber(State state, Side color) { // for ML
        int evalNo = 0;
        int turn = 1;

        ArrayList<OriginAndDestSquare> originAndDestSquares = LegalMoveGenerator.getAllLegalMoves(state, color);

        for (OriginAndDestSquare tempMove : originAndDestSquares) {
            Square s = tempMove.getOrigin();
            Piece p = state.getBoard().getPieceAt(s);

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

    public static int getEvalOfQL(State state, int depth) {
        final LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
        DQL ql = new DQL();
        ql.algo(state, state.getColor(), depth);
        return ql.getAvgValuesOfTable();
    }

    // return a value of a board for a given side taking into account the piece values and their corresponding board square position values
    public static int getBoardEvaluationNumber(List<PieceAndSquareTuple> nodePieceAndSquare, Side color, int turn) {
        int evalNo = 0;
        for (PieceAndSquareTuple<Piece,Square> ps : nodePieceAndSquare) {
            // if piece friendly add weight, else subtract opposite weight
            if (ps.getPiece().getColor()==color) {
                evalNo += ps.getPiece().getWeight();
                evalNo += getCorrectWeights(ps.getPiece(),turn)[ps.getSquare().getRank()-1][ps.getSquare().getFile()];
            } else {
                evalNo -= ps.getPiece().getWeight();
                evalNo -= getCorrectWeights(ps.getPiece(),turn)[ps.getSquare().getRank()-1][ps.getSquare().getFile()];
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

    private final static int[][] knightBoardWeightsB = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50} };
    private final static int[][] knightBoardWeightsW = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50} };

    private final static int[][] queenBoardWeightsB = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20} };
    private final static int[][] queenBoardWeightsW = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20} };

    private final static int[][] bishopBoardWeightsB = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20} };
    private final static int[][] bishopBoardWeightsW = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20} };

    private final static int[][] pawnBoardWeightsB = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {0,  0,  0,  0,  0,  0,  0,  0} };
    private final static int[][] pawnBoardWeightsW = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}};

    private final static int[][] rookBoardWeightsW = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {0,  0,  0,  5,  5,  0,  0,  0} };
    private final static int[][] rookBoardWeightsB = {
            {0,  0,  0,  5,  5,  0,  0,  0},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {5, 10, 10, 10, 10, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0} };

    private final static int[][] kingBoardWeightsMiddleGameW = {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {20, 30, 10,  0,  0, 10, 30, 20} };
    private final static int[][] kingBoardWeightsMiddleGameB = {
            {20, 30, 10,  0,  0, 10, 30, 20},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30}, };

    private final static int[][] kingBoardWeightsEndGameW = {
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50} };
    private final static int[][] kingBoardWeightsEndGameB = {
            {-50,-30,-30,-30,-30,-30,-30,-50},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-50,-40,-30,-20,-20,-30,-40,-50} };

}
