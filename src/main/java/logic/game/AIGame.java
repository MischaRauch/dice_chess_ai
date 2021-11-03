package logic.game;

import logic.Move;
import logic.State;
import logic.enums.Side;
import logic.enums.Validity;
import logic.player.AIPlayer;
import logic.player.ExpectiMiniMaxPlayer;
import logic.player.RandomMovesPlayer;

public class AIGame extends HumanGame {

    AIPlayer aiPlayer;

    public AIGame() {
        super();
        aiPlayer = new ExpectiMiniMaxPlayer(Side.BLACK);
    }

    //should ideally be called immediately in the GUI controller after the makeHumanMove returns
    //actually could be done just using Player objects instead of this class probably
    public Move makeAIMove() {
        Move move = aiPlayer.chooseMove(currentState);
        State newState = currentState.applyMove(move);
        previousStates.push(currentState);
        currentState = newState;
        move.setStatus(Validity.VALID);
        return move;
    }

}
