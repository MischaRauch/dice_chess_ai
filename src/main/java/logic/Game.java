package logic;

import logic.board.Board0x88;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.enums.Validity;

import java.util.LinkedList;

public class Game {
    static String openingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 1";

    public static void main(String[] args) {
        Game game = new Game();

        //pawn movement and capture seems to work
        Move move = new Move(Piece.WHITE_PAWN, Square.c2, Square.c4, 1, Side.WHITE);
        if (game.makeMove(move).getStatus() == Validity.VALID) {
            Move nextMove = new Move(Piece.BLACK_PAWN, Square.d7,Square.d5, 1, Side.BLACK);
            if (game.makeMove(nextMove).getStatus() == Validity.VALID) {
                Move nextMove2 = new Move(Piece.WHITE_PAWN, Square.c4,Square.d5, 1, Side.WHITE);
                if (game.makeMove(nextMove2).getStatus() == Validity.VALID) {
                    Move nextMove3 = new Move(Piece.BLACK_PAWN, Square.c7,Square.c6, 1, Side.BLACK);
                    if (game.makeMove(nextMove3).getStatus() == Validity.VALID) {
                        Move nextMove4 = new Move(Piece.WHITE_PAWN, Square.d5,Square.c6, 1, Side.WHITE);
                        if (game.makeMove(nextMove4).getStatus() == Validity.VALID) {
                            Move nextMove5 = new Move(Piece.BLACK_PAWN, Square.b7,Square.c6, 1, Side.BLACK);
                            game.makeMove(nextMove5);
                        }
                    }
                }
            }
        }
    }

    LinkedList<State> states; //TODO: what is this for?

    LinkedList<State> previousStates;

    State currentState;

    String[] boardFENConfig;
    LegalMoveEvaluator evaluator = new LegalMoveEvaluator();

    public Game(LinkedList<State> states, String[] boardFENConfig) {
        this.states = states;
        this.boardFENConfig = boardFENConfig;
    }

    public Game(String initialPosition) {
        currentState = new State(new Board0x88(initialPosition), 1, Side.WHITE);
        previousStates = new LinkedList<>();//Maybe should be stack or queue instead
    }

    public Game() {
        this(openingFEN);
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
            move.setStatus(Validity.VALID);

        } else {
            move.setInvalid();
        }

        //send back to GameboardConroller with updated validity flag
        return move;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Side getTurn() {
        return currentState.color;
    }

    public int getDiceRoll() {
        return currentState.diceRoll;
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
