package logic.game;

import logic.PieceAndSquareTuple;
import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.enums.Validity;
import logic.Move;
import logic.State;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class HumanGame extends Game {

    private final boolean DEBUG = false;

    // called for GUI to moves Tile
    public Move makeHumanMove(Move move) {
        if (evaluator.isLegalMove(move, currentState, true)) { //move legal

            State newState = currentState.applyMove(move);
            previousStates.push(currentState);

            // update pieceAndSquare in state
            updatePieceAndSquareState(newState,move);
            newState.printPieceAndSquare();

            currentState = newState;
            move.setStatus(Validity.VALID);

            // could also update pieceAndSquare here idk if it would make a difference

            processCastling();

        } else {
            move.setInvalid();
        }

        //send back to GUI with updated validity flag
        return move;
    }

    private void printArray(List<PieceAndSquareTuple> pieceAndSquare){
        System.out.println("printArray: ");
        for (PieceAndSquareTuple t : pieceAndSquare) {
            System.out.print(t.toString() + " | ");
        }
        System.out.println("Size: " + pieceAndSquare.size());
    }

    private void updatePieceAndSquareState(State newState, Move move) {
        // to avoid concurrency error
        List<PieceAndSquareTuple> list = new CopyOnWriteArrayList<PieceAndSquareTuple>();
        ListIterator litr = newState.getPieceAndSquare().listIterator();
        // loop thorugh pieceAndSquareState
        while(litr.hasNext()) {
            PieceAndSquareTuple t = (PieceAndSquareTuple) litr.next();
            if (t.getSquare() == move.getOrigin()) {
                if(DEBUG)System.out.println("skipped: " + t.getPiece().toString() + t.getSquare().toString());
            } else {
                list.add((PieceAndSquareTuple)t);
                if(DEBUG)System.out.println("added: " + t.getPiece().toString() + t.getSquare().toString());
            }
        }
        PieceAndSquareTuple tupleForLeavingSquare = new PieceAndSquareTuple(move.getPiece(), move.getDestination());
        list.add(tupleForLeavingSquare);
        if(DEBUG)System.out.println("added:2 " + tupleForLeavingSquare.getPiece().toString() + tupleForLeavingSquare.getSquare().toString());
        if(DEBUG)printArray(list);

        List<PieceAndSquareTuple> list2 = new CopyOnWriteArrayList<PieceAndSquareTuple>();

        ListIterator litr2 = list.listIterator();
        while(litr2.hasNext()) {
            PieceAndSquareTuple t = (PieceAndSquareTuple) litr2.next();
            if (tupleForLeavingSquare.getSquare()==t.getSquare() &&  litr2.nextIndex()==list.size()-1) {
                litr2.next();
                if(DEBUG)System.out.println("skipped: " + t.getPiece().toString() + t.getSquare().toString());
            } else if (tupleForLeavingSquare.getSquare()!=t.getSquare() && litr2.nextIndex()!=list.size()-1){
                list2.add(t);
                if(DEBUG)System.out.println("added:1 " + t.getPiece().toString() + t.getSquare().toString());
            } else if (litr2.nextIndex()==list.size()-1){
                list2.add(t);
                if(DEBUG)System.out.println("added:2 " + t.getPiece().toString() + t.getSquare().toString());
            }
        }
        list2.add(tupleForLeavingSquare);
        if(DEBUG)printArray(list2);

        newState.setPieceAndSquare(list2);
    }

}
