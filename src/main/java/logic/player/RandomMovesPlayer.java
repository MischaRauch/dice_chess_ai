package logic.player;

import logic.PieceAndSquareTuple;
import logic.enums.Side;
import logic.Move;
import logic.State;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class RandomMovesPlayer extends AIPlayer {

    private final boolean DEBUG2 = false;
    private final static Random randomly = new Random();
    public RandomMovesPlayer(Side color) {
        super(color);
    }

    @Override
    public Move chooseMove(State state) {
        List<Move> validMoves = getValidMoves(state);
        Move move = validMoves.get(randomly.nextInt(validMoves.size()));
        updatePieceAndSquareState(state, move);
        return move;
    }

    private void updatePieceAndSquareState(State state, Move move) {
        state.printPieceAndSquare();

        List<PieceAndSquareTuple> list = new CopyOnWriteArrayList<PieceAndSquareTuple>();
        ListIterator litr = state.getPieceAndSquare().listIterator();

        while(litr.hasNext()) {
            PieceAndSquareTuple t = (PieceAndSquareTuple) litr.next();
            if (t.getSquare() == move.getOrigin()) {
                if( DEBUG2)System.out.println("skipped: " + t.getPiece().toString() + t.getSquare().toString());
            } else {
                list.add((PieceAndSquareTuple)t);
                if( DEBUG2)System.out.println("added: " + t.getPiece().toString() + t.getSquare().toString());
            }
        }
        PieceAndSquareTuple tupleForLeavingSquare = new PieceAndSquareTuple(move.getPiece(), move.getDestination());
        list.add(tupleForLeavingSquare);
        if( DEBUG2)System.out.println("added:2 " + tupleForLeavingSquare.getPiece().toString() + tupleForLeavingSquare.getSquare().toString());

        List<PieceAndSquareTuple> list2 = new CopyOnWriteArrayList<PieceAndSquareTuple>();

        ListIterator litr2 = list.listIterator();
        while(litr2.hasNext()) {
            PieceAndSquareTuple t = (PieceAndSquareTuple) litr2.next();
            if (tupleForLeavingSquare.getSquare()==t.getSquare() &&  litr2.nextIndex()==list.size()-1) {
                litr2.next();
                if( DEBUG2)System.out.println("skipped: " + t.getPiece().toString() + t.getSquare().toString());
            } else if (tupleForLeavingSquare.getSquare()!=t.getSquare() && litr2.nextIndex()!=list.size()-1){
                list2.add(t);
                if( DEBUG2)System.out.println("added:1 " + t.getPiece().toString() + t.getSquare().toString());
            } else if (litr2.nextIndex()==list.size()-1){
                list2.add(t);
                if( DEBUG2)System.out.println("added:2 " + t.getPiece().toString() + t.getSquare().toString());
            }
        }
        list2.add(tupleForLeavingSquare);

        state.setPieceAndSquare(list2);
    }

}
