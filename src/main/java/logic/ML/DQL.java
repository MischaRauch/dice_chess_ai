package logic.ML;

import logic.Move;
import logic.State;
import logic.board.Board;

public class DQL {

    State aid;
    //Move

    public void algo() {
        Qtable currentQtable = creatingQtable();

        Board Initialstate = aid.getBoard();
        int convergence = 100;

        for (int i=0; i<convergence; i++) {

        }

    }

    public Qtable creatingQtable() { // creating a table for all pairs of state-action
        Qtable currentQtable = creatingQtable();
        return currentQtable;
    }


}
