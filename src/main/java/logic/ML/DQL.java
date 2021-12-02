package logic.ML;

import logic.Move;
import logic.State;
import logic.enums.Piece;
import logic.enums.Side;

import logic.algorithms.BoardStateEvaluator;
import logic.player.ExpectiMiniMaxPlayer;

import java.util.*;


public class DQL {

    Qtable currentQtable;
    double [][] Qvalues;
    ExpectiMiniMaxPlayer exp;
    State initialState;

    public void algo(State initialState, Side side, int depth) {
        currentQtable = new Qtable(initialState, side , depth);
        this.initialState = initialState;

        int stateSize = currentQtable.stateSpace.size();
        int actionSize = 4672; // 8x8x(8x7+8+9), this is the total possible actions a state can have at most

        Qvalues = new double[stateSize][actionSize]; // save the q-values in a separate table

        for (double[] row: Qvalues) {// fill the table with 0
            Arrays.fill(row, 0);
        }
        int numOfGames = 100;

        double explorationProb = 1; // this defines the prob. that the agent will explore
        double explorationDecay = 0.007;
        double minExplorationProb = 0.01; // prob. of exploration can go down until 0.01

        double gamma = 0.99; // discount factor, helps the algo to strive for a long-term high reward (0<gamma<1)
        double learningRate = 0.1;

        ArrayList<Double> totalRewardsOfEachEpisode = new ArrayList<>(); // to test the performance of the agent

        State currentState;
        Move action;

        for (int i=0; i < numOfGames; i++) {
            currentState = initialState;

            double reward;
            double gamesTotalReward = 0;
            boolean finished = false; // turn to true if king captured

            System.out.println("Game"+i);

            for (int j=0; j < depth/2; j++) {
                /* TODO, fix getting index (stateIndex is fixed)
                */
                System.out.println("action"+j);
                System.out.println("debugging");
//                currentQtable.stateSpace.get(0).board.printBoard();
//                currentQtable.stateSpace.get(currentQtable.accessStateIndex(initialState)).board.printBoard();
                System.out.println(currentQtable.accessStateIndex((initialState)));
                currentQtable.stateSpace.get(1).getBoard().printBoard();
                currentQtable.stateSpace.get(currentQtable.accessStateIndex(currentQtable.stateSpace.get(1))).getBoard().printBoard();
                //System.out.println("debugging");
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
                State newState = currentState.applyMove(action);
                System.out.println(currentQtable.accessStateIndex(newState));
                newState = newState.applyMove(currentQtable.randomMoveGenerator(newState, Side.getOpposite(side)));
                System.out.println(currentQtable.accessStateIndex(newState)+"needs to be 0<x<400 for step 1");
                System.out.println();

                reward = BoardStateEvaluator.getBoardEvaluationNumber(newState, currentQtable.currentSide);
                finished = didStateEnd(newState);

                // update value of Qtable
                int indexOfState = currentQtable.accessStateIndex(currentState);
                OriginAndDestSquare tempOriginAndDestSquare = new OriginAndDestSquare(action.getOrigin(), action.getDestination());

                // currentState.printPieceAndSquare();
                int indexOfAction = currentQtable.accessActionIndex(currentState, tempOriginAndDestSquare);

//                System.out.println();
//                System.out.println(currentQtable.accessStateIndex(currentState));
//                for (int s=0; s<10; s++) {
//                    System.out.println("-");
//                }
//                System.out.println("check new state, actual first");
//                newState.getBoard().printBoard();
//                System.out.println(currentQtable.accessStateIndex(newState));
//                // System.out.println(Qvalues.length);
//                System.out.println();

                Qvalues[indexOfState][indexOfAction] = (1-learningRate) * Qvalues[indexOfState][indexOfAction] + learningRate*(reward + gamma* maxValue(Qvalues, currentQtable.accessStateIndex(newState)));
                //System.out.println(Qvalues[indexOfState][indexOfAction]);
                gamesTotalReward += reward;
                currentState = newState;

                if (finished) {
                    break;
                }

             }

            //explorationProb = Math.max(minExplorationProb, Math.exp(-explorationDecay*Math.exp(1)));
            explorationProb -= explorationDecay;
            totalRewardsOfEachEpisode.add(gamesTotalReward);

        }

    }

    public boolean didStateEnd(State state) {
        return !(currentQtable.checkIfKingExist(state));
    }

    public Move getBestMove(State state, Side color) {
    state.getDiceRoll();
//        for(int i = 0; i < Qvalues[0].length; i++){
//                System.out.println(Qvalues[0][i]);
//            }


        int index = argmax(Qvalues, currentQtable.accessStateIndex(initialState));
        System.out.println(index);
        System.out.println(Qvalues[0][index]);
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
