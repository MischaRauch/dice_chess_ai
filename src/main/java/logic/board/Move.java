package logic.board;

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

    public enum Validity {
        VALID,
        INVALID,
        PROCESSING
    }
}
