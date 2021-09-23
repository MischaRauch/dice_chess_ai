package logic;

import logic.board.GameState;
import logic.board.Board;
import logic.board.Piece;

import java.util.EnumSet;
import java.util.LinkedList;
import logic.board.Move;

public class Game {

    LinkedList<State> states;
    LinkedList<GameState> prevstates;
    GameState currentState;
    String[] boardFENConfig;
    LegalMoveEvaluator evaluator = new LegalMoveEvaluator();

    public Game(LinkedList<State> states, String[] boardFENConfig) {
        this.states = states;
        this.boardFENConfig = boardFENConfig;
    }

    public Move makeMove(Move move) {
        if (evaluator.isLegalMove(move, currentState)) {
            GameState newGameState = currentState.applyMove(move);
            prevstates.add(currentState);
            currentState = newGameState;
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
