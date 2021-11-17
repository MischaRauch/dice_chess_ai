package logic;

import logic.enums.Side;
import logic.expectiminimax.ExpectiMiniMax;
import logic.expectiminimax.Tree;
import logic.game.AiAiGame;
import logic.game.Game;
import logic.player.ExpectiMiniMaxPlayer;
import logic.player.RandomMovesPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


import static logic.enums.Side.WHITE;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class MinimaxUnitTest {
    private Tree gameTree;
    private ExpectiMiniMax miniMax;

    @Before
    public void initMiniMaxUtility() {
        miniMax = new ExpectiMiniMax();
    }

    @Test
    public void givenMiniMax_whenConstructTree_thenNotNullTree() {
        assertNull(gameTree);
        miniMax.constructTree((new AiAiGame(new RandomMovesPlayer(WHITE), new ExpectiMiniMaxPlayer(Side.BLACK))));
        gameTree = miniMax.getTree();
        assertNotNull(gameTree);
    }

    @Test
    public void givenMiniMax_whenCheckWin_thenComputeOptimal() {
        miniMax.constructTree(new AiAiGame(new RandomMovesPlayer(WHITE), new ExpectiMiniMaxPlayer(Side.BLACK)));
        boolean result = miniMax.checkWin();
        assertTrue(result);
        miniMax.constructTree(new AiAiGame(new RandomMovesPlayer(WHITE), new ExpectiMiniMaxPlayer(Side.BLACK)));
        result = miniMax.checkWin();
        assertFalse(result);
    }
}
