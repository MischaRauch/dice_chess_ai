package logic.ML;

import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;

import logic.algorithms.BoardStateEvaluator;

import java.util.*;


public class DQL {

    Qtable currentQtable;
    double [][] Qvalues;

    public void algo(State initialState, Side side, int depth) {
        currentQtable = new Qtable(initialState, side , depth);

        int stateSize = currentQtable.stateSpace.size();
        int actionSize = 4672; // 8x8x(8x7+8+9), this is the total possible actions a state can have at most

        Qvalues = new double[stateSize][actionSize]; // save the q-values in a separate table

        for (double[] row: Qvalues) {// fill the table with 0
            Arrays.fill(row, 0);
        }
        int numOfEpisodes = 50;
        int maxIterationOfEpisode = 10;

        double explorationProb = 1; // this defines the prob. that the agent will explore
        double explorationDecay = 0.001;
        double minExplorationProb = 0.01; // prob. of exploration can go down until 0.01

        double gamma = 0.99; // discount factor, helps the algo to strive for a long-term high reward (0<gamma<1)
        double learningRate = 0.1;

        ArrayList<Double> totalRewardsOfEachEpisode = new ArrayList<>(); // to test the performance of the agent

        State currentState;
        Move action;

        for (int i=0; i < numOfEpisodes; i++) {
            currentState = initialState;

            double reward;
            double episodesTotalReward = 0;
            boolean finished = false; // turn to true if king captured

            for (int j=0; j < maxIterationOfEpisode; j++) {
                // take learned path or explore new actions
                System.out.println(currentState);

                if (Math.random() <= explorationProb) { // at first picking action will be totally random
                    action = currentQtable.randomMoveGenerator(currentState);
                    // (Piece piece, Square origin, Square destination, int diceRoll, Side side)
                } else {

                    int index = argmax(Qvalues, currentQtable.accessStateIndex(currentState));
                    ArrayList<OriginAndDestSquare> originAndDestSquares = currentQtable.accessStateValue(currentState);
                    OriginAndDestSquare tempMove = originAndDestSquares.get(index);
                    Piece p = currentState.getBoard().getPieceAt(tempMove.getOrigin());

                    action = new Move(p, tempMove.getOrigin(), tempMove.getDest(), Piece.getDiceFromPiece(p), side);
                }

                //apply chosen action and return the next state, reward and true if the episode is ended
                State newState = currentState.applyMove(action);

                reward = BoardStateEvaluator.getBoardEvaluationNumber(newState, currentQtable.currentSide); // TODO, to be added
                finished = didStateEnd(newState);

                // update value of Qtable
                int indexOfState = currentQtable.accessStateIndex(currentState);
                OriginAndDestSquare tempOriginAndDestSquare = new OriginAndDestSquare(action.getOrigin(), action.getDestination());

                // currentState.printPieceAndSquare();
                int indexOfAction = currentQtable.accessActionIndex(currentState, tempOriginAndDestSquare);

                // System.out.println(indexOfState);
                // System.out.println(indexOfAction);

                Qvalues[indexOfState][indexOfAction] = (1-learningRate) * Qvalues[indexOfState][indexOfAction] + learningRate*(reward + gamma* maxValue(Qvalues, currentQtable.accessStateIndex(currentState)));
                //System.out.println(Qvalues[indexOfState][indexOfAction]);
                episodesTotalReward += reward;
                currentState = newState;

                if (finished) {
                    break;
                }

             }

            explorationProb = Math.max(minExplorationProb, Math.exp(-explorationDecay*Math.exp(1)));
            totalRewardsOfEachEpisode.add(episodesTotalReward);

        }

    }

    public boolean didStateEnd(State state) {
        return (currentQtable.checkIfStateLastDepth(state) || currentQtable.checkIfKingExist(state));
    }

    public Move getBestMove(State state, Side color) {

        int index = argmax(Qvalues, 0); // TODO fix updating qValues
        System.out.println(index);
        ArrayList<OriginAndDestSquare> originAndDestSquares = currentQtable.accessStateValue(state);

        OriginAndDestSquare tempMove = originAndDestSquares.get(index);
        Piece p = state.getBoard().getPieceAt(tempMove.getOrigin());

        return (new Move(p, tempMove.getOrigin(), tempMove.getDest(), Piece.getDiceFromPiece(p), color));
    }

    public int argmax (double [][] qvalues, int stateIndex) {
        double count = 0;
        int indexOfMaxAction = -1; // if returns this somethings wrong

        for(int i = 0; i < qvalues[stateIndex].length; i++){

            if(qvalues[stateIndex][i] > count){
                count = qvalues[stateIndex][i];
                indexOfMaxAction = i;
            }
        }
       return indexOfMaxAction;
    }

    public double maxValue (double [][] qvalues, int stateIndex) {
        double count = 0;

        for(int i = 0; i < qvalues[stateIndex].length; i++){
            if(qvalues[stateIndex][i] > count){
                count = qvalues[stateIndex][i];
            }
        }
        return count;
    }
}
