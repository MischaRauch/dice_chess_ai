package logic.ML;

import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;
import logic.minimax.BoardStateGenerator;

import java.util.*;


public class DQL {

    State state;
    Qtable currentQtable;

    public void algo(Side side, int depth) {
        currentQtable = new Qtable(side , depth);
        int stateSize = currentQtable.stateSpace.size();
        int actionSize = 4672; // 8x8x(8x7+8+9), this is the total possible actions a state can have at most

        double [][] Qvalues = new double[stateSize][actionSize]; // save the q-values in a separate table
        Arrays.fill(Qvalues, 0); // fill the table with 0

        int numOfEpisodes = 400;
        int maxIterationOfEpisode = 500;

        double explorationProb = 1; // this defines the prob. that the agent will explore
        double explorationDecay = 0.001;
        double minExplorationProb = 0.01; // prob. of exploration can go down until 0.01

        double gamma = 0.99; // discount factor, helps the algo to strive for a long-term high reward (0<gamma<1)
        double learningRate = 0.1;

        ArrayList<Double> totalRewardsOfEachEpisode = new ArrayList<Double>(); // to test the performance of the agent

        Game game = Game.getInstance();
        State initialState = game.getCurrentState(); // get current state
        State currentState;

        Random rnd = new Random();
        Move action = null;

        for (int i=0; i < numOfEpisodes; i++) {

            currentState = initialState;

            double reward = 0;
            double episodesTotalReward = 0;
            boolean finished = false; // turn to true if king captured

            for (int j=0; j < maxIterationOfEpisode; j++) {

                // take learned path or explore new actions
                if (Math.random() <= explorationProb) { // at first picking action will be totally random
                    action = currentQtable.randomMoveGenerator(currentState);
                    // (Piece piece, Square origin, Square destination, int diceRoll, Side side)
                } else {

                    int index = argmax(Qvalues, currentQtable.accessStateIndex(currentState));
                    ArrayList<OriginAndDestSquare> originAndDestSquares = currentQtable.accessStateValue(currentState);
                    OriginAndDestSquare tempMove = originAndDestSquares.get(index);
                    Piece p = state.getBoard().getPieceAt((Square) tempMove.getOrigin());

                    action = new Move(p, (Square) tempMove.getOrigin(), (Square) tempMove.getDest(), Piece.getDiceFromPiece(p), side);
                }

                //apply chosen action and return the next state, reward and true if the episode is ended
                State newState = state.applyMove(action);
                reward = BoardStateGenerator.getBoardEvaluationNumber(newState); // TODO, to be added
                finished = didStateEnd(newState);

                // update value of Qtable
                int indexOfState = currentQtable.accessStateIndex(currentState);
                OriginAndDestSquare tempOriginAndDestSquare = new OriginAndDestSquare(action.getOrigin(), action.getDestination());
                int indexOfAction = currentQtable.accessActionIndex(currentState, tempOriginAndDestSquare);

                Qvalues[indexOfState][indexOfAction] = (1-learningRate) * Qvalues[indexOfState][indexOfAction] + learningRate*(reward + gamma* maxValue(Qvalues, currentQtable.accessStateIndex(currentState)));

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

    public int argmax (double [][] qvalues, int stateIndex) {
        double count = 0;
        int indexOfMaxAction = -1;

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
