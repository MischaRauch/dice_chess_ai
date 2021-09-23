package logic;

import java.util.LinkedList;

public class Game {

    LinkedList<State> states; //TODO: what is this for?

    LinkedList<State> previousStates;

    State currentState;

    String[] boardFENConfig;
    LegalMoveEvaluator evaluator = new LegalMoveEvaluator();

    public Game(LinkedList<State> states, String[] boardFENConfig) {
        this.states = states;
        this.boardFENConfig = boardFENConfig;
    }
    // add state to LinkedList
    public void updateState(State state) {
        states.add(state);
    }

    public Move makeMove(Move move) {
        if (evaluator.isLegalMove(move, currentState)) {

            State newState = currentState.applyMove(move);

            previousStates.add(currentState);
            currentState = newState;
            move.setStatus(Move.Validity.VALID);

        } else {
            move.setInvalid();
        }

        return move;
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
