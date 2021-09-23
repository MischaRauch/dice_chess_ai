package logic;

import logic.board.Piece;
import logic.board.Square;

public class Move {

    Piece piece;
    Square origin;
    Square destination;
    int diceRoll;

    Side color;
    Validity status;

    public Move(Piece piece, Square origin, Square destination, int diceRoll, Side color) {
        this.piece = piece;
        this.origin = origin;
        this.destination = destination;
        this.diceRoll = diceRoll;
        this.color = color;

        this.status = Validity.PROCESSING;
    }

    public Validity getStatus() {
        return status;
    }

    public void setStatus(Validity status) {
        this.status = status;
    }

    public void setValid() {
        this.status = Validity.VALID;
    }

    public void setInvalid() {
        this.status = Validity.VALID;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getOrigin() {
        return origin;
    }

    public Square getDestination() {
        return destination;
    }

    public int getDiceRoll() {
        return diceRoll;
    }

    public Side getColor() {
        return color;
    }

    public enum Validity {
        VALID,
        INVALID,
        PROCESSING
    }
}
