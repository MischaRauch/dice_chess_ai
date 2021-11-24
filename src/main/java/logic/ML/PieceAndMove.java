package logic.ML;

import logic.enums.Piece;
import logic.enums.Side;

import java.util.ArrayList;
import java.util.HashMap;


public class PieceAndMove {
    HashMap<Piece, ArrayList<Integer>> actionSpace; // saving each pieces' possible actions
    Side currentSide;

    public PieceAndMove(Side currentSide) {
        this.currentSide = currentSide;
    }

    public HashMap<Piece, ArrayList<Integer>> createActionSpace() { // creating map depending on side colour
        actionSpace = new HashMap<>();
        ArrayList<Integer> temp;

        if (currentSide == Side.WHITE) { // if white
            //pawn
            temp = new ArrayList<>();
            temp.add(16);
            temp.add(15);
            temp.add(17);
            actionSpace.put(Piece.WHITE_PAWN, temp);
            // knight
            temp = new ArrayList<>();
            temp.add(33);
            temp.add(31);
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
        } else { // if black
            //pawn
            temp = new ArrayList<>();
            temp.add(16);
            temp.add(15);
            temp.add(17);
            actionSpace.put(Piece.BLACK_PAWN, temp);
            // knight
            temp = new ArrayList<>();
            temp.add(33);
            temp.add(31);
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
    return actionSpace;
    }
}
