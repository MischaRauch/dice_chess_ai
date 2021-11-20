package logic.ML;

import logic.Move;
import logic.State;
import logic.board.Board;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

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

        for (int i=0; i < numOfEpisodes; i++) {

            Board currentState = Initialstate;
            double reward = 0;
            double episodesTotalReward = 0;

            for (int j=0; j < maxIterationOfEpisode; j++) {
                currentQtable.Qtable.values().toArray()
                // picking an action
                if (Math.random() < explorationProb) { // at first picking action will be totally random

                    // TODO, insert random move generator
                    Move action = new Move(Piece.WHITE_KNIGHT, Square.b2, Square.a3, 4, Side.WHITE);
                    // (Piece piece, Square origin, Square destination, int diceRoll, Side side)
                } else {

                }
             }

        }

    }

    public Qtable creatingQtable() { // creating a table for all pairs of state-action
        Qtable currentQtable = creatingQtable();
        return currentQtable;
    }


}
