package logic.ML;

import logic.LegalMoveGenerator;
import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.algorithms.BoardStateGenerator;

import java.util.*;

public class Qtable {

    ArrayList<OriginAndDestSquare> actionSpace;
    LinkedHashMap<State, ArrayList<OriginAndDestSquare>> Qtable; // size mxn where m (rows) is # of states and n (column) is # of actions
    ArrayList<State> stateSpace;
    Side currentSide;
    LegalMoveGenerator legalMoveGenerator;
    ArrayList<Integer> indexOfDepthOfAI = new ArrayList<>();
    int depth;

    public Qtable(State currentState, Side side, int depth) {
        this.Qtable = new LinkedHashMap<>();
        this.currentSide = side;
        this.depth = depth;
        ConstructQtable(currentState, depth);
    }


    public void ConstructQtable(State currentState, int depth) { // this table doesn't contain values, only info
        stateSpace = createStateSpace(currentState, depth);

        for (State state : stateSpace) {
            actionSpace = createActionSpace(state); // for each state create actionSpace
            Qtable.put(state, actionSpace); // adding actionSpace of the state
        }
    }

    public ArrayList<OriginAndDestSquare> createActionSpace(State state) {
        return legalMoveGenerator.getAllLegalMoves(state, currentSide);
    }

    public ArrayList<OriginAndDestSquare> accessStateValue(State state) {
        for ( Map.Entry<State, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            if (entry.getKey().equals1(state)) {
                return entry.getValue();
                }
            }
        return null;
    }

    public int accessStateIndex(State state) {
        int answer = 0;
        for (State tempState: stateSpace) {
            if (tempState.equals1(state)) {
                return answer;
            }
            answer++;
        }
        return answer;
    }

    public int accessActionIndex(State state, OriginAndDestSquare originAndDestSquare) { // TODO, decide what to do with -1

        for ( Map.Entry<State, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            if (entry.getKey().equals1(state)) {
                ArrayList<OriginAndDestSquare> temp = entry.getValue();
                for (int i=0; i<temp.size(); i++) {
                    if ((temp.get(i).getOrigin().getSquareNumber() == originAndDestSquare.getOrigin().getSquareNumber())
                        && (temp.get(i).getDest().getSquareNumber() == originAndDestSquare.getDest().getSquareNumber())) {
                        return i;
                    }
                }
            }
        }
        return -1; // shouldn't happen
    }

    public int addPieceWeights(State state, Side side) {
        int totalWeights = 0;

        Piece[] pieceList = state.getBoard().getBoard();
        for (Piece p: pieceList) {
            totalWeights += getWeightOf(side, p);
        }

        totalWeights += check(state, side); // if I lose king or get king give max or min number
        return totalWeights;
    }
    public int check(State state, Side side) { // with for loop make each move that gets king inf
        Piece p;
        boolean WExist = false;
        boolean BExist = false;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                p = state.getBoard().getPieceAt(Square.getSquare(rank, file));
                if (p.equals(Piece.WHITE_KING)) {
                    WExist = true;
                }
                if (p.equals(Piece.BLACK_KING)) {
                    BExist = true;
                }
            }
        }
        if (side.equals(Side.BLACK) && !WExist) { return Integer.MAX_VALUE;}
        else if(side.equals(Side.WHITE) && !BExist) {return Integer.MAX_VALUE;}
        else if (side.equals(Side.BLACK) && !BExist) {return Integer.MIN_VALUE;}
        else if(side.equals(Side.WHITE) && !WExist) {return Integer.MIN_VALUE;}

        return 0;
    }

    public int getWeightOf(Side side, Piece p) {
        if (side.equals(Side.BLACK)) {
            return switch (p) {
                case BLACK_PAWN -> 100;
                case BLACK_KNIGHT, BLACK_BISHOP -> 350;
                case BLACK_ROOK -> 525;
                case BLACK_QUEEN -> 1000;
                case BLACK_KING -> 200000;

                case WHITE_PAWN -> -100;
                case WHITE_KNIGHT, WHITE_BISHOP -> -350;
                case WHITE_ROOK -> -525;
                case WHITE_QUEEN -> -1000;
                case WHITE_KING -> -200000;
                default -> 0;
            };
        } else if(side.equals(Side.WHITE)) {
            return switch (p) {
                case BLACK_PAWN -> -100;
                case BLACK_KNIGHT, BLACK_BISHOP -> -350;
                case BLACK_ROOK -> -525;
                case BLACK_QUEEN -> -1000;
                case BLACK_KING -> -200000;

                case WHITE_PAWN -> 100;
                case WHITE_KNIGHT, WHITE_BISHOP -> 350;
                case WHITE_ROOK -> 525;
                case WHITE_QUEEN -> 1000;
                case WHITE_KING -> 200000;
                default -> 0;
            };
        }
        return 0;
    }

    public int getIndexOfBestMove(int[][] qvalues, ArrayList<OriginAndDestSquare> moves, Piece p, State initial, int b) {
        double count = 0;
        int indexOfMaxAction = -1; // if returns this somethings wrong
        int a = 0;
        ArrayList<Integer> doableMoves = new ArrayList<>();

        for (OriginAndDestSquare move : moves) {
            if (p.equals(initial.getBoard().getPieceAt(move.getOrigin()))) {
                doableMoves.add(a);
                if(qvalues[b][a] > count){
                    count = qvalues[b][a];
                    indexOfMaxAction = a;
                }
            }
            a++;
        }

        if (count == 0) {
            int index = (int) (Math.random() * doableMoves.size());
            return doableMoves.get(index);
        }
        return indexOfMaxAction;
    }
//    public boolean checkIfStateLastDepth(State state) {
//        int a;
//        if (indexOfDepthOfAI.size() < 2) {
//            a = 0;
//        } else {
//            a = indexOfDepthOfAI.get(indexOfDepthOfAI.size() - 1) - indexOfDepthOfAI.get(indexOfDepthOfAI.size() - 2);
//        }
//
//        for (int i = a; i<indexOfDepthOfAI.size(); i++) {
//            if (stateSpace.get(i) == state) {
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean checkIfKingsExist(State state) {
        Piece p;
        boolean WExist = false;
        boolean BExist = false;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                p = state.getBoard().getPieceAt(Square.getSquare(rank, file));
                if (p.equals(Piece.WHITE_KING)) {
                    WExist = true;
                }
                if (p.equals(Piece.BLACK_KING)) {
                    BExist = true;
                }
            }
        }
        return (WExist && BExist);
    }

    public Move randomMoveGenerator(State state, Side side) {
        Random r = new Random();
        ArrayList<OriginAndDestSquare> allMoves = legalMoveGenerator.getAllLegalMoves(state, side);
        OriginAndDestSquare tempMove;
        int num = r.nextInt(allMoves.size());

        tempMove = allMoves.get(num);
        Piece p = state.getBoard().getPieceAt(tempMove.getOrigin());
        return (new Move(p, tempMove.getOrigin(), tempMove.getDest(), Piece.getDiceFromPiece(p), side));
    }

    public ArrayList<State> createStateSpace(State currentState, int depth) {

        ArrayList<State> possibleStatesOfCurrentBoard;

        ArrayList<State> opponentState = new ArrayList<>();
        ArrayList<Integer> indexOfDepthOfOpponent = new ArrayList<>();

        ArrayList<State> stateSpace = new ArrayList<>();
        ArrayList<Integer> indexOfDepthOfAI = new ArrayList<>(); // this shows from which index the first state of that depth
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
                currentDepthOfAI++;

            }

            else { // for opponent side

                int totalStates = 0;
                int toWhere;
                int a;

                if (currentDepthOfAI == 0) {
                    toWhere = indexOfDepthOfAI.get(currentDepthOfAI);
                    a = indexOfDepthOfAI.get(currentDepthOfAI);
                } else {
                    toWhere = indexOfDepthOfAI.get(currentDepthOfAI);
                    a = indexOfDepthOfAI.get(currentDepthOfAI-1) + 1;
                }

                for (int j=a; j<=toWhere; j++) {
                    State tempState = stateSpace.get(j);
                    possibleStatesOfCurrentBoard = BoardStateGenerator.getPossibleBoardStates(tempState, currentSide);
                    totalStates += possibleStatesOfCurrentBoard.size();
                    opponentState.addAll(possibleStatesOfCurrentBoard);
                }
                indexOfDepthOfOpponent.add(totalStates + indexOfDepthOfOpponent.get(currentDepthOfOpponent));
                currentDepthOfOpponent++;
            }
        }
//        for (int a=0; a<stateSpace.size(); a++) {
//            stateSpace.get(a).getBoard().printBoard();
//        }
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
