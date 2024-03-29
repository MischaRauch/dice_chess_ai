package logic;


import logic.enums.Piece;
import logic.enums.Side;
import logic.enums.Square;
import logic.enums.Validity;

public class Move {

    Piece piece;
    Square origin;
    Square destination;
    int diceRoll;

    public Piece promotionPiece;
    public boolean promotionMove = false;

    Side side;
    Validity status;

    public Square enPassant = Square.INVALID;
    boolean enPassantMove = false;
    boolean enPassantCapture = false;
    public Square castlingRookDestination = Square.INVALID;
    boolean AIMove = false;

    public Move(Piece piece, Square origin, Square destination, int diceRoll, Side side) {
        this.piece = piece;
        this.origin = origin;
        this.destination = destination;
        this.diceRoll = diceRoll;
        this.side = side;
        promotionPiece = piece;

        this.status = Validity.PROCESSING;
    }

    public Validity getStatus() {
        return status;
    }

    public boolean isEnPassantCapture() {
        return enPassantCapture;
    }

    public boolean isPromotionMove() {
        return promotionMove;
    }

    public boolean isAIMove() {
        return AIMove;
    }

    public Piece getPromotionPiece() {
        return promotionPiece;
    }

    public void setStatus(Validity status) {
        this.status = status;
    }

    public void setValid() {
        this.status = Validity.VALID;
    }

    public void setInvalid() {
        this.status = Validity.INVALID;
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

    public Side getSide() {
        return side;
    }

    public Square getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(Square enPassant) {
        this.enPassant = enPassant;
    }

    public boolean isEnPassantMove() {
        return enPassantMove;
    }

    public void setEnPassantMove(boolean enPassantMove) {
        this.enPassantMove = enPassantMove;
    }

    public void setEnPassantCapture(boolean enPassantCapture) {
        this.enPassantCapture = enPassantCapture;
    }

    @Override
    public String toString() {
        return "Moved: " + piece +
                " from: " + origin +
                ", to: " + destination;
    }

    public String stylized() {
        return origin + " ➞ " + destination;
    }
}
