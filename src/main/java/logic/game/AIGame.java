package logic.game;

import gui.controllers.MainContainerController;
import logic.enums.Side;
import logic.enums.Validity;
import logic.Move;
import logic.State;
import logic.player.AIPlayer;
import logic.player.MiniMaxPlayer;

public class AIGame extends HumanGame {

    private final AIPlayer aiPlayer;

    public AIGame(int depth) {
        this(new MiniMaxPlayer(depth,Side.BLACK));
    }

    public AIGame(AIPlayer aiPlayer) {
        super();
        this.aiPlayer = aiPlayer;
    }

    public AIPlayer getAiPlayerAiGame(){ return aiPlayer;}
    //public Side getAiPlayerSide(){ return  Side.BLACK;}
    public Side getAiPlayerSide(){ return aiPlayer.getColor();}

    //should ideally be called immediately in the GUI controller after the makeHumanMove returns
    //actually could be done just using Player objects instead of this class probably
    public Move makeAIMove() {
        Move move = aiPlayer.chooseMove(currentState);
        State newState = currentState.applyMove(move);
        previousStates.push(currentState);

        checkGameOver(move);

        currentState = newState;
        move.setStatus(Validity.VALID);
        processCastling();
        MainContainerController.getInstance().updateTurn(currentState.getColor());
        return move;
    }

}
