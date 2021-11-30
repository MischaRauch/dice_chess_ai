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
    ArrayList<Integer> indexOfDepthOfAI = new ArrayList<>();
    int depth;

    public Qtable(State currentState, Side side, int depth) {
        this.Qtable = new HashMap<>();
        this.currentSide = side;
        this.depth = depth;
        ConstructQtable(currentState, depth);
    }


    public void ConstructQtable(State currentState, int depth) { // this table doesn't contain values, only info
        stateSpace = createStateSpace(currentState, depth);
        for (int i=0; i<stateSpace.size(); i++) {
            // System.out.println(stateSpace.get(i).getBoard().getFEN());
        }

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

    public int accessActionIndex(State state, OriginAndDestSquare originAndDestSquare) { // TODO, decide what to do with -1

        for ( Map.Entry<State, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            if (entry.getKey() == state) {
                ArrayList<OriginAndDestSquare> temp = entry.getValue();
                for (int i=0; i<temp.size(); i++) {
                    // System.out.println(temp.get(i) +" why "+ originAndDestSquare);
                    if (temp.get(i).getOrigin().getSquareNumber() == originAndDestSquare.getOrigin().getSquareNumber()
                        && temp.get(i).getDest().getSquareNumber() == originAndDestSquare.getDest().getSquareNumber()) {
                        return i;
                    }
                }
            }
        }
        return 0; // shouldn't happen
    }

    public boolean checkIfStateLastDepth(State state) {
        int a;
        if (indexOfDepthOfAI.size() < 2) {
            a = 0;
        } else {
            a = indexOfDepthOfAI.get(indexOfDepthOfAI.size() - 1) - indexOfDepthOfAI.get(indexOfDepthOfAI.size() - 2);
        }

        for (int i = a; i<indexOfDepthOfAI.size(); i++) {
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

    public ArrayList<State> createStateSpace(State currentState, int depth) {
        /* TODO, fix this (general idea is to create all possible board states of the player,
            then save next all possible states, then again apply from there
        */
        ArrayList<State> opponentState = new ArrayList<>();
        ArrayList<Integer> opponentIndexOfDepth = new ArrayList<>();

        stateSpace = new ArrayList<>();
        ArrayList<State> possibleStatesOfCurrentBoard;

        ArrayList<Integer> indexOfDepthOfOpponent = new ArrayList<>();
        indexOfDepthOfAI = new ArrayList<>(); // this shows from which index the first state of that depth
        // starts and at which index it ends. Ex: indexOfDepth[2] will return e.g. 125, this means states of depth 2 ends
        // at 125, to get where it starts just do indexOfDepth[2] - indexOfDepth[1]

        stateSpace.add(currentState); // first step
        indexOfDepthOfAI.add(0);
        indexOfDepthOfOpponent.add(0);

        int currentDepthOfAI = 0;
        int currentDepthOfOpponent = 0;

        for (int i = 1; i < depth*2; i++) {

            if (i % 2 == 0) { // for AI side
                int totalStates = 0;
                int toWhere = indexOfDepthOfOpponent.get(currentDepthOfOpponent);
                int a = indexOfDepthOfOpponent.get(currentDepthOfOpponent-1);

                for (int j=a; j<toWhere; j++) {
                    State tempState = opponentState.get(j);
                    possibleStatesOfCurrentBoard = BoardStateGenerator.getPossibleBoardStates(tempState, Side.getOpposite(currentSide));
                    totalStates += possibleStatesOfCurrentBoard.size();
                    stateSpace.addAll(possibleStatesOfCurrentBoard);
                }
                indexOfDepthOfAI.add(totalStates + indexOfDepthOfAI.get(currentDepthOfAI));
                // System.out.println("AI" + indexOfDepthOfAI.get(currentDepthOfAI + 1));
                currentDepthOfAI++;

            }

            else if (i % 2 == 1){ // for opponent side

                int totalStates = 0;
                int toWhere;
                int a;

                if (currentDepthOfAI == 0) {
                    toWhere = indexOfDepthOfAI.get(currentDepthOfAI) + 1;
                    a = indexOfDepthOfAI.get(currentDepthOfAI);
                } else {
                    toWhere = indexOfDepthOfAI.get(currentDepthOfAI);
                    a = indexOfDepthOfAI.get(currentDepthOfAI-1) + 1;
                }

                for (int j=a; j<toWhere; j++) {
                    State tempState = stateSpace.get(j);
                    possibleStatesOfCurrentBoard = BoardStateGenerator.getPossibleBoardStates(tempState, currentSide);
                    totalStates += possibleStatesOfCurrentBoard.size();
                    opponentState.addAll(possibleStatesOfCurrentBoard);
                }
                indexOfDepthOfOpponent.add(totalStates + indexOfDepthOfOpponent.get(currentDepthOfOpponent));
                // System.out.println("opponent" + indexOfDepthOfOpponent.get(currentDepthOfOpponent + 1));
                currentDepthOfOpponent++;
            }


        }
        return stateSpace;
    }

//    public Arrays<int> accessStatesByDepth(ArrayList<State> allStates, ArrayList<Integer> infoOfIndex, int depth) {
//        ArrayList<State> tempStates = new ArrayList<>();
//        int toWhere = infoOfIndex.get(depth);
//        int a = infoOfIndex.get(depth-1);
//
//        for (int i = a; i<toWhere; i++) {
//            if (stateSpace.get(i) == state) {
//                return true;
//            }
//        }
//        return
//    }

    // TODO, here maybe prune moves somehow
    public HashMap<Piece, ArrayList<Integer>> actionPruning(State state) { // remove actions that's not possible for that state
        return null;
    }

}
