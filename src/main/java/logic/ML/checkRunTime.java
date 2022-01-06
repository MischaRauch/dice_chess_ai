package logic.ML;

import logic.State;
import logic.board.Board;
import logic.board.Board0x88;
import logic.enums.Side;

public class checkRunTime {

    static Board startBoard = new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1");
    static State state;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        state = new State(startBoard, 1, Side.WHITE);
        DQL ql = new DQL();
        ql.algo(state, state.getColor(), 2);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
