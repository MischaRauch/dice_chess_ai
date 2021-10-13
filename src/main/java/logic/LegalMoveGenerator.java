package logic;

import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import java.util.ArrayList;

public class LegalMoveGenerator {

    public ArrayList<Square> getLegalMoves(State state, Square squareOrigin, Piece piece, Side side) {
        LegalMoveEvaluator evaluator = new LegalMoveEvaluator();
        ArrayList<Square> legalMoves = new ArrayList<>();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                if(evaluator.isLegalMove(new Move(piece,squareOrigin,Square.getSquare(rank,file),2,side),state)) {
                    legalMoves.add(Square.getSquare(rank,file));
                    System.out.println("loop rank: " + rank + " - loop file: " + file);
                    System.out.println("square rank: " + Square.getSquare(rank,file).getRank() +
                            " - square file: " + Square.getSquare(rank,file).getFile());
                }
            }
        }
        return legalMoves;
    }

}
