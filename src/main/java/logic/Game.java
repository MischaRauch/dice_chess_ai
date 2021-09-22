package logic;

import java.util.LinkedList;

public class Game {

    LinkedList<State> states;
    String[] boardFENConfig;

    public Game(LinkedList<State> states, String[] boardFENConfig) {
        this.states = states;
        this.boardFENConfig = boardFENConfig;
    }
    // add state to LinkedList
    public void updateState(State state) {
        states.add(state);
    }
    // remove state from LinkedList
    public void undoState(State state) {
        states.remove(state); // remove last or any state?

    }

    //optional
    public void redoState(State state) {
        //
    }



}
