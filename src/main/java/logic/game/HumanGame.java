package logic.game;

import logic.enums.Piece;
import logic.enums.Validity;
import logic.Move;
import logic.State;

public class HumanGame extends Game {

    // called for GUI to moves Tile
    public Move makeHumanMove(Move move) {
        if (evaluator.isLegalMove(move, currentState, true)) { //move legal

            State newState = currentState.applyMove(move);
            previousStates.push(currentState);

            // set previous location empty
            newState.getBoardPieces()[move.getOrigin().getRank()-1][move.getOrigin().getFile()]=Piece.EMPTY;
            System.out.println("rank " + (move.getOrigin().getRank()-1));
            System.out.println("file " + move.getOrigin().getFile());
            // set destination to current piece
            newState.getBoardPieces()[move.getDestination().getRank()-1][move.getDestination().getFile()]=move.getPiece();
            System.out.println("rank " + (move.getDestination().getRank()-1));
            System.out.println("file " + move.getDestination().getFile());
            newState.boardPiecesToString();

            currentState = newState;
            move.setStatus(Validity.VALID);

            // could also update boardPieces here idk if it would make a difference

            processCastling();

        } else {
            move.setInvalid();
        }

        //send back to GUI with updated validity flag
        return move;
    }

}
