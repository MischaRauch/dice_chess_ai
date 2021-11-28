package logic.player;

import logic.ML.DQL;
import logic.ML.Qtable;
import logic.Move;
import logic.State;
import logic.enums.Side;
import logic.game.Game;


import java.util.List;

public class QLPlayer extends AIPlayer{
    private final int depth;
    DQL ql = new DQL();

    public QLPlayer(int depth, Side color) {
        super(color);
        this.depth = depth;
    }

    Qtable currentQtable;
    Game game = Game.getInstance();
    State initialState = game.getCurrentState();
    int Stateindex = currentQtable.accessStateIndex(initialState);
    double[][] Qvalues;


    @Override
    public Move chooseMove(State state){ // TODO, implement this

        List<Move> validMoves = getValidMoves(state);

        ql.algo(color,depth);
        double maxvalue = MaxvalueforState(initialState);

        int MoveMaxIndex = ql.argmax(Qvalues,Stateindex);
        Move chosenMove = validMoves.get(MoveMaxIndex);  // choose the move with the maximum value for the state
        return chosenMove;

        }


        public double MaxvalueforState(State state)
        {

            double value = ql.maxValue(Qvalues,Stateindex);
            return value;

        }





}
