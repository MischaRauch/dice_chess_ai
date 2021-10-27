package logic;

public class PieceAndTurnDeathTuple<Piece, Integer> {
    public final Piece x;
    public final int y;

    public PieceAndTurnDeathTuple(Piece x, Integer y) {
        this.x = x;
        this.y = (int) y;
    }
    public Piece getPiece() {
        return x;
    }

    public int getTurnDeath() {
        return y;
    }
}