package logic;

import java.util.LinkedList;

public class Game {

    LinkedList<State> states;

    LinkedList<State> prevstates;

    State currentState;

    String[] boardFENConfig;
    LegalMoveEvaluator evaluator = new LegalMoveEvaluator();

    public Game(LinkedList<State> states, String[] boardFENConfig) {
        this.states = states;
        this.boardFENConfig = boardFENConfig;
    }

    public Move makeMove(Move move) {
        if (evaluator.isLegalMove(move, currentState)) {
            State newState = currentState.applyMove(move);

            prevstates.add(currentState);
            currentState = newState;
            move.setStatus(Move.Validity.VALID);


        } else {
            move.setInvalid();
        }

        return move;
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
