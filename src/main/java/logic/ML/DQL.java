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
import java.util.HashMap;

public class DQL {

    State state;


    public void algo() {
        Qtable currentQtable = creatingQtable();
        int stateSize = currentQtable.stateSpace.size();
        int actionSize = 4672; // 8x8x(8x7+8+9)

        int [][] Qvalues = new int [stateSize][actionSize];
        Arrays.fill(Qvalues, 0);

        int numOfEpisodes = 400;
        int maxIterationOfEpisode = 500;

        double explorationProb = 1;
        double explorationDecay = 0.001;
        double minExplorationProb = 0.01;

        double gamma = 0.99;
        double learningRate = 0.1;

        ArrayList<Integer> totalRewardsOfEachEpisode = new ArrayList<Integer>();

        Board Initialstate = state.getBoard();
        Random rnd = new Random();
        Move action = null;
        for (int i=0; i < numOfEpisodes; i++) {

            Board currentState = Initialstate;
            double reward = 0;
            double episodesTotalReward = 0;
            boolean finished = false;
            Object a = currentQtable.Qtable.values().toArray();

            currentQtable.stateSpace.get(5);
            currentQtable.actionSpace.
            for (int j=0; j < maxIterationOfEpisode; j++) {

                // take learned path or explore new actions
                if (Math.random() < explorationProb) { // at first picking action will be totally random

                    // TODO, insert random move generator
                    action = new Move(Piece.WHITE_KNIGHT, Square.b2, Square.a3, 4, Side.WHITE);
                    // (Piece piece, Square origin, Square destination, int diceRoll, Side side)
                } else {

                    // TODO, get action info with index
                    int index = argmax(Qvalues, 1);
                    action = new Move(Piece.WHITE_KNIGHT, Square.b2, Square.a3, 4, Side.WHITE);
                }

                // apply chosen action and return the next state, reward and true if the episode is ended
                State newState = state.applyMove(action);
                BoardStateEvaluator.getBoardEvaluationNumber(newState); //ugh
                // how to know if episode is ended

                currentQtable[currentState, action]

             }

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

    public Qtable creatingQtable() { // creating a table for all pairs of state-action
        Qtable currentQtable = creatingQtable();
        return currentQtable;
    }


}
