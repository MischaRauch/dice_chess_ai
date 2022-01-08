package logic.player;

import logic.Move;
import logic.State;
import logic.enums.Side;

import java.util.*;

import static logic.enums.Piece.*;

public class QTablePlayer extends AIPlayer {

    private final boolean DEBUG = true;
    private Map<Move, Integer> moveMap = new HashMap<Move, Integer>();

    public QTablePlayer(Side color) { super(color);}

    @Override
    public String getNameAi() { return "QTablePlayer";}

    @Override
    public Move chooseMove(State state) {
        List<Move> validMoves = getValidMoves(state);
        //at the beginning of a move clear the map
        moveMap.clear();
        for (Move move : validMoves) {
            moveMap.put(move,0);
        }
        bestCapture(state,validMoves);
        //get the best capture move
        Move key = Collections.max(moveMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("KEY "+key);
        System.out.println("Value "+moveMap.get(key));
        if (moveMap.get(key) != 0) {
            System.out.println("QTablePlayer: Color: " + this.color.toString() + " Next optimal Move: (key) " + key);
            return key;
        }
        else {
            MiniMaxPlayer ept = new MiniMaxPlayer(7,this.color);
            System.out.println("QTablePlayer: Color: " + this.color.toString() + " Next optimal Move: (ept.chooseMove(state)) " + ept.chooseMove(state));
            return ept.chooseMove(state);
        }

    }


    public int[][] buildQTable(State state) {

        int[][] table = new int[8][8];


        return table;
    }

    public void bestCapture(State state, List<Move> validMoves) {
        int bestMoveScore = 0;
        for (Move move : validMoves) {
            if (!state.getBoard().isEmpty(move.getDestination())) {
                System.out.println("good move "+move);
                System.out.println("Target PIECE "+state.getBoard().getPieceAt(move.getDestination()).getType());
                switch (state.getBoard().getPieceAt(move.getDestination()).getType()) {
                    case PAWN -> {
                        if (bestMoveScore < 100) {
                            moveMap.put(move,100);
                            bestMoveScore = 100;
                        }
                    }
                    case KNIGHT, BISHOP -> {
                        if (bestMoveScore < 350) {
                            moveMap.put(move,350);
                            bestMoveScore = 350;
                        }
                    }
                    case ROOK -> {
                        if (bestMoveScore < 525) {
                            moveMap.put(move,525);
                            bestMoveScore = 525;
                        }
                    }
                    case QUEEN -> {
                        if (bestMoveScore < 1000) {
                            moveMap.put(move,1000);
                            bestMoveScore = 1000;
                        }
                    }
                    case KING -> {
                        if (bestMoveScore < 10000) {
                            moveMap.put(move,10000);
                            bestMoveScore = 10000;
                        }
                    }
                }
            }
        }
    }
}
