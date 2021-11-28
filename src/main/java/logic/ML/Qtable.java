package logic.ML;

import logic.LegalMoveGenerator;
import logic.Move;
import logic.PieceAndSquareTuple;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;
import logic.algorithms.BoardStateGenerator;


import java.util.*;
import java.util.stream.Collectors;

import static logic.enums.Piece.getDiceFromPiece;

public class Qtable {

    ArrayList<OriginAndDestSquare> actionSpace;
    HashMap<State, ArrayList<OriginAndDestSquare>> Qtable; // size mxn where m (rows) is # of states and n (column) is # of actions
    ArrayList<State> stateSpace;
    Side currentSide;
    LegalMoveGenerator legalMoveGenerator;
    ArrayList<Integer> indexOfDepth;
    int depth;

    public Qtable(Side side, int depth) {
        this.Qtable = new HashMap<>();
        this.currentSide = side;
        this.depth = depth;
        ConstructQtable(depth);
    }


    public void ConstructQtable(int depth) { // this table doesn't contain values, only info
        stateSpace = createStateSpace(depth);

        for (int i=0; i<stateSpace.size(); i++) {
            actionSpace = createActionSpace(stateSpace.get(i)); // for each state create actionSpace

            for ( int j=0; j<actionSpace.size(); j++) {
                Qtable.put(stateSpace.get(i), actionSpace); // adding actionSpace of the state
            }
        }
    }

    public ArrayList<OriginAndDestSquare> createActionSpace(State state) {
        return legalMoveGenerator.getAllLegalMoves(state, currentSide);
    }

    public ArrayList<OriginAndDestSquare> accessStateValue(State state) {
        for ( Map.Entry<State, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            if (entry.getKey() == state) {
                return entry.getValue();
                }
            }
        return null;
    }

    public int accessStateIndex(State state) {
        int answer = 0;
        for ( Map.Entry<State, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            answer++;
            if (entry.getKey() == state) {
                return answer;
            }
        }
        return answer;
    }

    public int accessActionIndex(State state, OriginAndDestSquare originAndDestSquare) {

        for ( Map.Entry<State, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            if (entry.getKey() == state) {
                ArrayList<OriginAndDestSquare> temp = entry.getValue();
                for (int i=0; i<temp.size(); i++) {
                    if (temp.get(i) == originAndDestSquare) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public boolean checkIfStateLastDepth(State state) {
        int a = indexOfDepth.get(depth) - indexOfDepth.get(depth-1);
        for (int i = a; i<indexOfDepth.size(); i++) {
            if (stateSpace.get(i) == state) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfKingExist(State state) {
        Piece p = null;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                p = state.getBoard().getPieceAt(Square.getSquare(rank, file));
                if (p == Piece.WHITE_KING || p == Piece.BLACK_KING) {
                    return true;
                }
            }
        }
        return false;
    }

    public Move randomMoveGenerator(State state) {
        Random r = new Random();
        ArrayList<OriginAndDestSquare> allMoves = createActionSpace(state);
        OriginAndDestSquare tempMove;
        int num = r.nextInt(allMoves.size());

        tempMove = allMoves.get(num);
        Piece p = state.getBoard().getPieceAt((Square) tempMove.getOrigin());
        return (new Move(p, (Square) tempMove.getOrigin(), (Square) tempMove.getDest(), Piece.getDiceFromPiece(p), currentSide));
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

            possibleStatesOfCurrentBoard = BoardStateGenerator.getPossibleBoardStates(currentState, currentSide);
            indexOfDepth.add(possibleStatesOfCurrentBoard.size() - indexOfDepth.get(i));
            stateSpace.addAll(possibleStatesOfCurrentBoard);
        }
        return stateSpace;
    }

    // TODO, here maybe prune moves somehow
    public HashMap<Piece, ArrayList<Integer>> actionPruning(State state) { // remove actions that's not possible for that state
        return null;
    }

}
