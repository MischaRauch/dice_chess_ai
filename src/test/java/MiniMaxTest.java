import logic.Config;
import logic.Move;
import logic.algorithms.minimax.MiniMax;
import logic.algorithms.minimax.Node;
import logic.game.AiAiGame;
import logic.game.Game;
import logic.player.MiniMaxPlayer;
import logic.player.RandomMovesPlayer;
import org.junit.jupiter.api.Test;

import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;
import static org.junit.Assert.assertTrue;

public class MiniMaxTest {

    private final MiniMax miniMax = new MiniMax();
    Game game = new AiAiGame(new MiniMaxPlayer(3, WHITE),
            new RandomMovesPlayer(BLACK), Config.OPENING_FEN);

    @Test
    public void correctEvaluationFirstMovePawnOrKnight() {
        miniMax.constructTree(3, game.getCurrentState());
        Node bestChild = miniMax.findBestChild(miniMax.getTree().getRoot().getChildren(), game.getCurrentState().getDiceRoll());
        Move bestMove = bestChild.getMove();
        assertTrue(bestMove.getDiceRoll() == 1 || bestMove.getDiceRoll() == 2);
    }

}
