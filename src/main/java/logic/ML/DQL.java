package logic.ML;

import logic.Move;
import logic.State;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.expectiminimax.BoardStateEvaluator;
import logic.game.Game;

import java.util.*;


public class DQL {

    State state;

    public void algo(Side side, int depth) {
        Qtable currentQtable = new Qtable(side , depth);
        int stateSize = currentQtable.stateSpace.size();
        int actionSize = 4672; // 8x8x(8x7+8+9), this is the total possible actions a state can have at most

        int [][] Qvalues = new int [stateSize][actionSize]; // save the q-values in a separate table
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

        for (int i=0; i < numOfEpisodes; i++) { // this will be handled when changed to DQL

            currentState = initialState;
            double reward = 0;
            double episodesTotalReward = 0;
            boolean finished = false; // turn to true if king captured

            for (int j=0; j < maxIterationOfEpisode; j++) {

                // take learned path or explore new actions
                if (Math.random() <= explorationProb) { // at first picking action will be totally random
                    // TODO, insert random move generator
                    action = new Move(Piece.WHITE_KNIGHT, Square.b2, Square.a3, 4, Side.WHITE);
                    // (Piece piece, Square origin, Square destination, int diceRoll, Side side)
                } else {

                    int index = argmax(Qvalues, 0);
                    currentQtable.accessStateValue(currentState);

                    action = new Move(Piece.WHITE_KNIGHT, Square.b2, Square.a3, 4, Side.WHITE);
                }

                // TODO, apply chosen action and return the next state, reward and true if the episode is ended
                State newState = state.applyMove(action);
                // update value of Qtable
                currentQtable[currentState, action] = (1-learningRate) * currentQtable[currentState, action] +learningRate*(reward + gamma* maxValue(Qvalues[newState]));
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

    public int argmax (int [][] qvalues, int stateIndex) {
        int count = 0;

        for(int i = 0; i < qvalues[stateIndex].length; i++){
            if(qvalues[stateIndex][i] > count){
                count = qvalues[stateIndex][i];
            }
        }
       return count;
    }

    public int maxValue (State state) {
        return 0;
    }
}
