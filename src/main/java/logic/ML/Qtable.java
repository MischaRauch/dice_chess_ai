package logic.ML;

import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static logic.expectiminimax.BoardStateGenerator.getPossibleBoardStates;


public class Qtable {

    PieceAndMove actionSpace; // at map each key can only have 1 value
    Map<Piece[][], PieceAndMove> Qtable; // size mxn where m (rows) is # of states and n (column) is # of actions
    ArrayList<Piece[][]> stateSpace;
    Side currentSide;

    public Qtable(Side side) {
        Qtable = new Hashtable<>();
        currentSide = side;
        actionSpace = new PieceAndMove(currentSide);
    }

    public void ConstructQtable(ArrayList<Piece[][]> stateSpace, Map<Piece, ArrayList<Integer>> actionSpace) {
        stateSpace = createStateSpace( 1);

        for (int i=0; i<stateSpace.size(); i++) {
            for ( Map.Entry<Piece, ArrayList<Integer>> entry : actionSpace.entrySet()) {
                Qtable.put(stateSpace.get(i), (PieceAndMove) entry);

            }
        }
    }

    public ArrayList<Piece[][]> createStateSpace(int depth) { // later will implement the depth feature, atm only 1 move
        Game game = Game.getInstance();
        Piece[][] currentState = game.getCurrentState().getBoardPieces();
        stateSpace.add(currentState);
        ArrayList<Piece[][]> possibleStatesOfCurrentBoard = getPossibleBoardStates(currentState);

        for (int i=0; i<possibleStatesOfCurrentBoard.size(); i++) {
            stateSpace.add(possibleStatesOfCurrentBoard.get(i));
        }
        return stateSpace;
    }

    public PieceAndMove createActionSpace() { // creating full action space
        actionSpace = new PieceAndMove(currentSide);
        return actionSpace;
    }

    public Map<String, Integer> actionPruning() { // remove actions that's not possible
        return null;
    }

}
