package logic.player;

import logic.Move;
import logic.State;
import logic.enums.Side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QTablePlayer extends AIPlayer {

    private final boolean DEBUG = true;

    public QTablePlayer(Side color) { super(color);}

    @Override
    public Move chooseMove(State state) {
        System.out.println("HELLLLOOOO ");
        List<Move> validMoves = getValidMoves(state);
        //int[][] qTable = buildQTable(state);
        int[] test = new int[validMoves.size()];
        ArrayList<Move> goodMoves = new ArrayList<>(validMoves.size());
        for (Move move : validMoves) {
            if (!state.getBoard().isEmpty(move.getDestination())) {
                //test[i] = 20;
                System.out.println("good move "+move);
                goodMoves.add(move);

            }
        }
        Arrays.sort(test);
        //System.out.println("Test "+ Arrays.toString(test));
        if (DEBUG) {
            System.out.println("Size "+goodMoves.size());
        }
        if (goodMoves.size() != 0) {
            System.out.println("CHOSSEN "+ goodMoves.get(0));
            System.out.println("CHOSSEN "+ goodMoves.get(0));
            return goodMoves.get(0);
        }
        else {
            return validMoves.get(test.length -1);
        }
    }

    public int[][] buildQTable(State state) {

        int[][] table = new int[8][8];


        return table;
    }
}
