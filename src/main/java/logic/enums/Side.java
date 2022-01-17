package logic.enums;

public enum Side {
    WHITE,
    BLACK,
    NEUTRAL;

    public char asChar() {
        return switch (this) {
            case WHITE -> 'W';
            case BLACK -> 'B';
            default -> 'N';
        };
    }

    public static Side getOpposite(Side color) {
        if (color == Side.WHITE) {
            return Side.BLACK;
        }
        return Side.WHITE;
    }

    public boolean opponent(Side side) {
        return this != NEUTRAL && side != this;
    }

}
