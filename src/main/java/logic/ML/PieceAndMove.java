package logic.ML;

import logic.enums.Piece;
import logic.enums.Side;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class PieceAndMove {
    Map<Piece, ArrayList<Integer>> actionSpace;

    public PieceAndMove(Side currentSide) {
        actionSpace = new Hashtable<>();

        if (currentSide == Side.WHITE) {
            //pawn
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(16);
            temp.add(15);
            temp.add(17);
            actionSpace.put(Piece.WHITE_PAWN, temp);
            // knight
            temp = new ArrayList<>();
            temp.add(33); // KnightUpRight
            temp.add(31); // KnightUpLeft
            temp.add(18);
            temp.add(14);
            temp.add(-33);
            temp.add(-31);
            temp.add(-18);
            temp.add(-14);
            actionSpace.put(Piece.WHITE_KNIGHT, temp);
            // Bishop
            temp = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                temp.add(15 * i);
                temp.add(17 * i);
                temp.add(-15 * i);
                temp.add(-17 * i);
            }
            actionSpace.put(Piece.WHITE_BISHOP, temp);
            // rook
            temp = new ArrayList<>();
            for (int i = 1; i < 9; i++) {
                temp.add(16 * i);
                temp.add(1 * i);
                temp.add(-16 * i);
                temp.add(-1 * i);
            }
            actionSpace.put(Piece.WHITE_ROOK, temp);
            // Queen
            temp = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                temp.add(15 * i);
                temp.add(17 * i);
                temp.add(-15 * i);
                temp.add(-17 * i);
            }
            for (int i = 1; i < 9; i++) {
                temp.add(16 * i);
                temp.add(1 * i);
                temp.add(-16 * i);
                temp.add(-1 * i);
            }
            actionSpace.put(Piece.WHITE_QUEEN, temp);
            // King
            temp = new ArrayList<>();
            temp.add(15);
            temp.add(17);
            temp.add(-15);
            temp.add(-17);
            temp.add(16);
            temp.add(1);
            temp.add(-16);
            temp.add(-1);
            actionSpace.put(Piece.WHITE_KING, temp);
        } else {
            //pawn
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(16);
            temp.add(15);
            temp.add(17);
            actionSpace.put(Piece.BLACK_PAWN, temp);
            // knight
            temp = new ArrayList<>();
            temp.add(33); // KnightUpRight
            temp.add(31); // KnightUpLeft
            temp.add(18);
            temp.add(14);
            temp.add(-33);
            temp.add(-31);
            temp.add(-18);
            temp.add(-14);
            actionSpace.put(Piece.BLACK_KNIGHT, temp);
            // Bishop
            temp = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                temp.add(15 * i);
                temp.add(17 * i);
                temp.add(-15 * i);
                temp.add(-17 * i);
            }
            actionSpace.put(Piece.BLACK_BISHOP, temp);
            // rook
            temp = new ArrayList<>();
            for (int i = 1; i < 9; i++) {
                temp.add(16 * i);
                temp.add(1 * i);
                temp.add(-16 * i);
                temp.add(-1 * i);
            }
            actionSpace.put(Piece.BLACK_ROOK, temp);
            // Queen
            temp = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                temp.add(15 * i);
                temp.add(17 * i);
                temp.add(-15 * i);
                temp.add(-17 * i);
            }
            for (int i = 1; i < 9; i++) {
                temp.add(16 * i);
                temp.add(1 * i);
                temp.add(-16 * i);
                temp.add(-1 * i);
            }
            actionSpace.put(Piece.BLACK_QUEEN, temp);
            // King
            temp = new ArrayList<>();
            temp.add(15);
            temp.add(17);
            temp.add(-15);
            temp.add(-17);
            temp.add(16);
            temp.add(1);
            temp.add(-16);
            temp.add(-1);
            actionSpace.put(Piece.BLACK_KING, temp);
        }

    }
}
