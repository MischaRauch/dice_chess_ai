package logic.player;


import logic.Move;
import logic.State;
import logic.enums.Side;
import logic.expectiminimax.MiniMax;
import logic.expectiminimax.Node;

public class MiniMaxPlayer extends AIPlayer {

    private final int depth;

    public MiniMaxPlayer(int depth, Side color) {
        super(color);
        this.depth = depth;
    }

    @Override
    public Move chooseMove(State state) {
        long start = System.nanoTime();
        MiniMax miniMax = new MiniMax();
        int depth = this.depth;
        miniMax.constructTree(depth, state);
        Node bestChild = miniMax.findBestChild(true, miniMax.getTree().getRoot().getChildren(), state.diceRoll);
        System.out.println("Optimal Move: " + bestChild.getMove().toString());
        Move chosenMove = bestChild.getMove();

        long end = System.nanoTime();
        System.out.println("MiniMaxPlayer: Elapsed Time to generate tree and find optimal move: " + (end - start));
        // update piece and square state
        state = getUpdatedPieceAndSquareState(state, chosenMove);
        state.printPieceAndSquare();
        return chosenMove;
    }

}
