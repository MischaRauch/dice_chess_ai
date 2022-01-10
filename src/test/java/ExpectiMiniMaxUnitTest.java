import logic.Config;
import logic.Move;
import logic.algorithms.expectiminimax.ExpectiMiniMax;
import logic.enums.Side;
import logic.game.AiAiGame;
import logic.game.Game;
import logic.player.ExpectiMiniMaxPlayer;
import logic.player.RandomMovesPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

public class ExpectiMiniMaxUnitTest {
    private final ExpectiMiniMax expectiMiniMax = new ExpectiMiniMax();
    Game game = new AiAiGame(new ExpectiMiniMaxPlayer(3, Side.WHITE),
            new RandomMovesPlayer(Side.BLACK), Config.OPENING_FEN);

    @Test
    public void correctEvaluationFirstMoveKnight() {
        expectiMiniMax.constructTree(3, game.getCurrentState());
        Move bestMove = expectiMiniMax.getBestMoveForBestNode();
        assertTrue(bestMove.getDiceRoll() == 2);
    }

}
