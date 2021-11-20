package logic.ML;

import logic.enums.Piece;
import logic.enums.Side;
import logic.game.Game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static logic.expectiminimax.BoardStateGenerator.getPossibleBoardStates;

public class Qtable {

    Map<Piece, ArrayList<Integer>> actionSpace;
    HashMap<Piece[][], PieceAndMove> Qtable; // size mxn where m (rows) is # of states and n (column) is # of actions
    ArrayList<Piece[][]> stateSpace;
    Side currentSide;
    int StepCounter = 0; // would be good to use this to increase negative reward each turn


    public Qtable(Side side) {
        Qtable = new HashMap<>();
        currentSide = side;
        actionSpace = new PieceAndMove(currentSide); // creating all action space
    }

    public void ConstructQtable(Map<Piece, ArrayList<Integer>> actionSpace) { // this table doesn't contain values, only info
        stateSpace = createStateSpace( 1);

        for (int i=0; i<stateSpace.size(); i++) {
            for ( Map.Entry<Piece, ArrayList<Integer>> entry : actionSpace.entrySet()) {
                Qtable.put(stateSpace.get(i), (PieceAndMove) entry);
            }
        }
    }

    public void getMove(Map<Piece, ArrayList<Integer>> actionSpace) { // this table doesn't contain values, only info
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

    public void addZeros() {
        // for (int i; i<)
    }
}
