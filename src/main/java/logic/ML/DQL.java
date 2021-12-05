package logic.ML;

import logic.Move;
import logic.State;
import logic.algorithms.BoardStateEvaluator;
import logic.enums.Piece;
import logic.enums.Side;
import logic.player.ExpectiMiniMaxPlayer;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.max;


public class DQL {

    Qtable currentQtable;
    int[][] Qvalues;
    ExpectiMiniMaxPlayer exp;
    State initialState;

    public void algo(State initialState, Side side, int depth) {
        currentQtable = new Qtable(new State(initialState), side, depth);
        this.initialState = initialState;

        int stateSize = currentQtable.stateSpace.size();
        int actionSize = 4672; // 8x8x(8x7+8+9), this is the total possible actions a state can have at most

        Qvalues = new int[stateSize][actionSize]; // save the q-values in a separate table

        for (int[] row : Qvalues) {// fill the table with 0
            Arrays.fill(row, 0);
        }
        double numOfGames = 1000;

        double explorationProb = 1; // this defines the prob. that the agent will explore
        double explorationDecay = 1 / numOfGames;
        double minExplorationProb = 0.01; // prob. of exploration can go down until 0.01

        double gamma = 0.99; // discount factor, helps the algo to strive for a long-term high reward (0<gamma<1)
        double learningRate = 0.1;

        ArrayList<Integer> totalRewardsOfEachEpisode = new ArrayList<>(); // to test the performance of the agent

        State currentState;
        Move action;

        for (int i = 0; i < numOfGames; i++) {
            currentState = new State(initialState);

            int reward;
            int gamesTotalReward = 0;
            boolean finished = false; // turn to true if king captured

            //System.out.println("Game"+i);

            for (int j = 0; j < depth / 2; j++) {
                //System.out.println("action"+j);

                // take learned path or explore new actions
                if (Math.random() <= explorationProb) { // at first picking action will be totally random
                    action = currentQtable.randomMoveGenerator(currentState, side);
                } else {
                    int index = argmax(Qvalues, currentQtable.accessStateIndex(currentState));
                    ArrayList<OriginAndDestSquare> originAndDestSquares = currentQtable.accessStateValue(currentState);
                    OriginAndDestSquare tempMove = originAndDestSquares.get(index);
                    Piece p = currentState.getBoard().getPieceAt(tempMove.getOrigin());

                    action = new Move(p, tempMove.getOrigin(), tempMove.getDest(), Piece.getDiceFromPiece(p), side);
                }
                //apply chosen action and return the next state, reward and true if the episode is ended
                State newState = new State(currentState.applyMove(action));

                newState = newState.applyMove(currentQtable.randomMoveGenerator(newState, Side.getOpposite(side)));
                // code line above can be changed with some better algo (example given below) which could give better results but would take more time
                //QTablePlayer help = new QTablePlayer(Side.getOpposite(side));
                //newState = newState.applyMove(help.chooseMove(newState));

                reward = getReward(newState, side);
                finished = didStateEnd(newState);

                // update value of Qtable
                int indexOfState = currentQtable.accessStateIndex(currentState);
                OriginAndDestSquare tempOriginAndDestSquare = new OriginAndDestSquare(action.getOrigin(), action.getDestination());
                int indexOfAction = currentQtable.accessActionIndex(currentState, tempOriginAndDestSquare);

                Qvalues[indexOfState][indexOfAction] = (int) ((1 - learningRate) * Qvalues[indexOfState][indexOfAction] + learningRate * (reward + gamma * maxValue(Qvalues, currentQtable.accessStateIndex(newState))));

                gamesTotalReward += reward;
                currentState = newState;
                if (finished) break;
            }
            if (explorationProb >= minExplorationProb) {explorationProb -= explorationDecay;}
            totalRewardsOfEachEpisode.add(gamesTotalReward);

        }
    }

    public boolean didStateEnd(State state) {
        return !(currentQtable.checkIfKingsExist(state));
    }

    public int getReward(State state, Side side) {
        int reward = 0;
        reward += BoardStateEvaluator.getBoardEvaluationNumber(state, currentQtable.currentSide) / 10;
//        System.out.println(BoardStateEvaluator.getBoardEvaluationNumber(state, currentQtable.currentSide)/10);
//        System.out.println(currentQtable.addPieceWeights(state, side)*50);
        reward += currentQtable.addPieceWeights(state, side) * 50;
        return reward;
    }

    public Move getBestMove(State state, Side color) {
        Piece tempP = Piece.getPieceFromDice(initialState.getDiceRoll(), initialState.getColor()); // get piece that needs to be equal
        int a = currentQtable.accessStateIndex(initialState);

        ArrayList<OriginAndDestSquare> originAndDestSquares = currentQtable.accessStateValue(state);

        int index = currentQtable.getIndexOfBestMove(Qvalues, originAndDestSquares, tempP, initialState, a);
        OriginAndDestSquare tempMove = originAndDestSquares.get(index);
        //System.out.println("wut"); // if this prints then problem is not here :))
        return (new Move(tempP, tempMove.getOrigin(), tempMove.getDest(), Piece.getDiceFromPiece(tempP), color));
    }

    public int argmax(int[][] qvalues, int stateIndex) {
        int count = 0;
        int indexOfMaxAction = -1; // if returns this somethings wrong
        for (int i = 0; i < qvalues[stateIndex].length; i++) {
            if (qvalues[stateIndex][i] > count) {
                count = qvalues[stateIndex][i];
                indexOfMaxAction = i;
            }
        }
        if (count == 0) {
            return (int) (Math.random() * currentQtable.accessStateValue(currentQtable.stateSpace.get(stateIndex)).size());
        }
        return indexOfMaxAction;
    }

    public int maxValue(int[][] qvalues, int stateIndex) {
        int count = 0;

        for (int i = 0; i < qvalues[stateIndex].length; i++) {
            if (qvalues[stateIndex][i] > count) {
                count = qvalues[stateIndex][i];
            }
        }
        return count;
    }
}
