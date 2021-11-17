package logic.enums;

public enum Side {
    WHITE,
    BLACK,
    NEUTRAL;

    public Side getOpposite(Side color) {
        if (color==Side.WHITE) {
            return Side.BLACK;
        }
        return Side.WHITE;
    }

}
