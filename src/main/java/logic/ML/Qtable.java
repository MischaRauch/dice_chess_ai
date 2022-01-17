package logic.ML;

import logic.*;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.algorithms.BoardStateGenerator;

import java.util.*;

public class Qtable {

    ArrayList<OriginAndDestSquare> actionSpace;
    LinkedHashMap<List<PieceAndSquareTuple>, ArrayList<OriginAndDestSquare>> Qtable; // size mxn where m (rows) is # of states and n (column) is # of actions
    List<List<PieceAndSquareTuple>> stateSpace;
    Side currentSide;
    State initialState;
    int depth;

    public Qtable(State currentState, Side side, int depth) {
        this.Qtable = new LinkedHashMap<>();
        this.currentSide = side;
        this.depth = depth;
        this.initialState = new State(currentState);
        ConstructQtable(initialState, depth);
    }


    public void ConstructQtable(State currentState, int depth) { // this table doesn't contain values, only info
        currentState = new State(currentState.getBoard(), currentState.diceRoll, currentSide);
        stateSpace = createStateSpace(currentState.getPieceAndSquare(), depth);

        for (List<PieceAndSquareTuple> state : stateSpace) {
            actionSpace = createActionSpace(state); // for each state create actionSpace
            Qtable.put(state, actionSpace); // adding actionSpace of the state
        }

    }

    public ArrayList<OriginAndDestSquare> createActionSpace(List<PieceAndSquareTuple> state) {
        return LegalMoveGenerator.getAllLegalMovesOpt(state, currentSide);
    }

    public ArrayList<OriginAndDestSquare> accessStateValue(List<PieceAndSquareTuple> state) {
        for (Map.Entry<List<PieceAndSquareTuple>, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            if (equalsForTuple(entry.getKey(), state)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean equalsForTuple(List<PieceAndSquareTuple> state1, List<PieceAndSquareTuple> state2) {
        boolean answer = false;
        for (PieceAndSquareTuple temp1 : state1) {
            Square sq1 = (Square) temp1.getSquare();
            Piece p1 = (Piece) temp1.getPiece();

            for (PieceAndSquareTuple temp2 : state2) {
                Square sq2 = (Square) temp2.getSquare();
                Piece p2 = (Piece) temp2.getPiece();
                if ((p1.getUnicode() == p2.getUnicode()) && (sq1.getBoardIndex() == sq2.getBoardIndex())) {
                    answer = true;
                }
            }
            if (answer) {
                answer = false;
            } else {
                return false;
            }
        }
        return true;
    }
    public int accessStateIndex(List<PieceAndSquareTuple> state) {
        int answer = 0;
        for (List<PieceAndSquareTuple> tempState : stateSpace) {
            if (equalsForTuple(tempState, state)) {
                return answer;
            }
            answer++;
        }
        return answer;
    }

    public int accessActionIndex(List<PieceAndSquareTuple> state, OriginAndDestSquare originAndDestSquare) {
        for (Map.Entry<List<PieceAndSquareTuple>, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            if (equalsForTuple(entry.getKey(), state)) {
                ArrayList<OriginAndDestSquare> temp = entry.getValue();
                for (int i = 0; i < temp.size(); i++) {
                    if ((temp.get(i).getOrigin().getSquareNumber() == originAndDestSquare.getOrigin().getSquareNumber())
                            && (temp.get(i).getDest().getSquareNumber() == originAndDestSquare.getDest().getSquareNumber())) {
                        return i;
                    }
                }
            }
        }
        return -1; // means action in that state doesn't exist
    }

    public int addPieceWeights(State state, Side side) {
        int totalWeights = 0;

        Piece[] pieceList = state.getBoard().getBoard();
        for (Piece p : pieceList) {
            totalWeights += getWeightOf(side, p);
        }

        totalWeights += check(state, side); // if I lose king or get king give max or min number
        return totalWeights;
    }

    public int check(State state, Side side) { // this method checks if the move makes the player lose/capture king
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
        if (side.equals(Side.BLACK) && !WExist) {
            return Integer.MAX_VALUE;
        } else if (side.equals(Side.WHITE) && !BExist) {
            return Integer.MAX_VALUE;
        } else if (side.equals(Side.BLACK) && !BExist) {
            return Integer.MIN_VALUE;
        } else if (side.equals(Side.WHITE) && !WExist) {
            return Integer.MIN_VALUE;
        }

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
        } else if (side.equals(Side.WHITE)) {
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
        int indexOfMaxAction = -1; // if returns this something is wrong
        int a = 0;
        ArrayList<Integer> doableMoves = new ArrayList<>();

        for (OriginAndDestSquare move : moves) {
            if (p.equals(initial.getBoard().getPieceAt(move.getOrigin()))) {
                doableMoves.add(a);
                if (qvalues[b][a] > count) {
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
        ArrayList<OriginAndDestSquare> allMoves = LegalMoveGenerator.getAllLegalMovesML(state, side);
        OriginAndDestSquare tempMove;
        int num = r.nextInt(allMoves.size());

        tempMove = allMoves.get(num);
        Piece p = state.getBoard().getPieceAt(tempMove.getOrigin());
        return (new Move(p, tempMove.getOrigin(), tempMove.getDest(), Piece.getDiceFromPiece(p), side));
    }

    public List<List<PieceAndSquareTuple>> createStateSpace(List<PieceAndSquareTuple> givenState, int depth) {
        List<List<PieceAndSquareTuple>> possibleBoardStates;
        List<List<PieceAndSquareTuple>> opponentState = new ArrayList<>();
        ArrayList<Integer> indexOfDepthOfOpponent = new ArrayList<>();

        List<List<PieceAndSquareTuple>> stateSpace = new ArrayList<>();
        ArrayList<Integer> indexOfDepthOfAI = new ArrayList<>(); // this shows from which index the first state of that depth
        // starts and at which index it ends. Ex: indexOfDepth[2] will return e.g. 125, this means states of depth 2 ends
        // at 125, to get where it starts just do indexOfDepth[2] - indexOfDepth[1]

        stateSpace.add(givenState); // first step
        indexOfDepthOfAI.add(0);
        indexOfDepthOfOpponent.add(0);

        int currentDepthOfAI = 0;
        int currentDepthOfOpponent = 0;

        for (int i = 1; i < depth * 2; i++) {

            if (i % 2 == 0) { // for AI side
                int totalStates = 0;
                int toWhere = indexOfDepthOfOpponent.get(currentDepthOfOpponent);
                int a = indexOfDepthOfOpponent.get(currentDepthOfOpponent - 1);

                for (int j = a; j < toWhere; j++) {

                    List<PieceAndSquareTuple> tempState = opponentState.get(j);
                    possibleBoardStates = getStates(tempState, Side.getOpposite(currentSide));

                    totalStates += possibleBoardStates.size();
                    stateSpace.addAll(possibleBoardStates);
                }
                indexOfDepthOfAI.add(totalStates + indexOfDepthOfAI.get(currentDepthOfAI));
                currentDepthOfAI++;
            } else { // for opponent side

                int totalStates = 0;
                int toWhere;
                int a;

                if (currentDepthOfAI == 0) {
                    toWhere = indexOfDepthOfAI.get(currentDepthOfAI);
                    a = indexOfDepthOfAI.get(currentDepthOfAI);
                } else {
                    toWhere = indexOfDepthOfAI.get(currentDepthOfAI);
                    a = indexOfDepthOfAI.get(currentDepthOfAI - 1) + 1;
                }

                for (int j = a; j <= toWhere; j++) {

                    List<PieceAndSquareTuple> tempState = stateSpace.get(j);

                    possibleBoardStates = getStates(tempState, currentSide);
                    totalStates += possibleBoardStates.size();
                    opponentState.addAll(possibleBoardStates);
                }
                indexOfDepthOfOpponent.add(totalStates + indexOfDepthOfOpponent.get(currentDepthOfOpponent));
                currentDepthOfOpponent++;
            }
        }
        return stateSpace;
    }

    public List<List<PieceAndSquareTuple>> getStates(List<PieceAndSquareTuple> givenState, Side side) {
        List<List<PieceAndSquareTuple>> states = new ArrayList<>();
        // loop through for all 6 dice numbers and generate all possible states
        for (int i = 1; i < 7; i++) {
            List<List<PieceAndSquareTuple>> temp = BoardStateGenerator.getPossibleBoardStates(givenState, side, i);
            states.addAll(temp);
        }
        return states;
    }
    public int getActionSize() {
        int biggestActionSize = 0;
        for (Map.Entry<List<PieceAndSquareTuple>, ArrayList<OriginAndDestSquare>> entry : Qtable.entrySet()) {
            if (entry.getValue().size() > biggestActionSize) {
                biggestActionSize = entry.getValue().size();
            }
        }
        return biggestActionSize;
    }
}


