package logic;

import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.enums.Legality;

public class Move {

    Piece piece;
    Square origin;
    Square destination;
    int diceRoll;

    Side color;
    Legality status;

    public Move(Piece piece, Square origin, Square destination, int diceRoll, Side color) {
        this.piece = piece;
        this.origin = origin;
        this.destination = destination;
        this.diceRoll = diceRoll;
        this.color = color;

        this.status = Legality.PROCESSING;
    }

    public Legality getStatus() {
        return status;
    }

    public void setStatus(Legality status) {
        this.status = status;
    }

    public void setValid() {
        this.status = Legality.VALID;
    }

    public void setInvalid() {
        this.status = Legality.VALID;
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

}
