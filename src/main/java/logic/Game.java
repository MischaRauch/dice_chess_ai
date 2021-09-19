package logic;

import java.util.LinkedList;

public class Game {

    LinkedList<State> states;
    String[] boardFENConfig;

    public Game(LinkedList<State> states, String[] boardFENConfig) {
        this.states = states;
        this.boardFENConfig = boardFENConfig;
    }

    public void updateState(State state) {
        // add state to LinkedList
    }

    //optional
    public void undoState(State state) {
        // remove state from LinkedList
    }

    //optional
    public void redoState(State state) {
        //
    }



}
