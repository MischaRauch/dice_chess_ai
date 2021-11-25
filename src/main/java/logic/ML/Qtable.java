package logic.ML;

import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Qtable {

    ArrayList<originAndDest> actionSpace;
    HashMap<State, ArrayList<originAndDest>> Qtable; // size mxn where m (rows) is # of states and n (column) is # of actions
    ArrayList<State> stateSpace;
    Side currentSide;
    PieceAndMove pieceAndMove;

    public Qtable(Side side, int depth) {
        Qtable = new HashMap<>();
        currentSide = side;
        pieceAndMove = new PieceAndMove(currentSide);
        ConstructQtable(depth);
    }


    public void ConstructQtable(int depth) { // this table doesn't contain values, only info
        stateSpace = createStateSpace(depth);

        for (int i=0; i<stateSpace.size(); i++) {
            actionSpace = createActionSpace(stateSpace.get(i));

            for ( int j=0; j<actionSpace.size(); j++) {
                Qtable.put(stateSpace.get(i), actionSpace); // adding actionSpace of the state
            }
        }

    }

    public ArrayList<originAndDest> createActionSpace(State state) {
        // TODO, get all possible originAndDest tuple of the state
        return null;
    }
//    public ArrayList<Integer> accessActionValue(Piece pieceName) {
//        for ( Map.Entry<Piece, ArrayList<Integer>> entry : actionSpace.entrySet()) {
//            if (entry.getKey() == pieceName) {
//                return entry.getValue();
//            }
//        }
//        return null;
//    }

    public ArrayList<originAndDest> accessStateValue(State state) {
        for ( Map.Entry<State, ArrayList<originAndDest>> entry : Qtable.entrySet()) {
            if (entry.getKey() == state) {
                return entry.getValue();
                }
            }
        return null;
    }

    public ArrayList<State> createStateSpace(int depth) { // here may happen small index errors, needs test
        Game game = Game.getInstance();
        State currentState = game.getCurrentState(); // get current state
        stateSpace.add(currentState);
        ArrayList<State> possibleStatesOfCurrentBoard;
        ArrayList<Integer> indexOfDepth = new ArrayList<Integer>(); // this shows from which index the first state of that depth
        // starts and at which index it ends. Ex: indexOfDepth[2] will return e.g. 125, this means states of depth 2 ends
        // at 125, to get where it starts just do indexOfDepth[2] - indexOfDepth[1]
        indexOfDepth.add(0);

        for (int i = 0; i < depth; i++) {
            possibleStatesOfCurrentBoard = getPossibleBoardStates(currentState);
            indexOfDepth.add(possibleStatesOfCurrentBoard.size() - indexOfDepth.get(i));

            for (int j = 0; j < possibleStatesOfCurrentBoard.size(); j++) {
                stateSpace.add(possibleStatesOfCurrentBoard.get(j));
            }
        }
        return stateSpace;
    }

    private ArrayList<State> getPossibleBoardStates(State currentState) {
        // TODO, implement a new method that returns all possible states from the current state
        return null;
    }

    // TODO, here can possibly prune the moves with getting valid moves method
    public HashMap<Piece, ArrayList<Integer>> actionPruning(State state) { // remove actions that's not possible for that state
        return null;
    }

    public ArrayList<originAndDest> getActionSpace(Side side) {

    }
}
