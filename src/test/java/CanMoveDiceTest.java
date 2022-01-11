import logic.State;
import logic.board.Board0x88;
import logic.enums.Side;
import org.junit.jupiter.api.Test;

import static logic.Dice.canMove;
import static logic.enums.Piece.PAWN;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CanMoveDiceTest {

    @Test
    void testCanMoveWhitePawn() {
        State state = new State(new Board0x88("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1 1"), 1, Side.WHITE);
        System.out.println("HELLO " + canMove(PAWN, state));
        assertTrue(canMove(PAWN, state));
    }

    @Test
    void test() {
        boolean test = true;
        assertTrue(test);
    }

}
