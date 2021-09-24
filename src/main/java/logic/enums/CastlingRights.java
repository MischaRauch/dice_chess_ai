package logic.enums;

public enum CastlingRights {
    WHITE_QUEEN_SIDE('Q'), //ordinal 0
    WHITE_KING_SIDE('K'),  //ordinal 1
    BLACK_QUEEN_SIDE('q'),
    BLACK_KING_SIDE('k'),

    NONE('o');

    char value;

    CastlingRights(char castlingSide) {
        this.value = castlingSide;
    }

    public char getCharValue() {
        return  value;
    }

    public CastlingRights getByChar(char c) {
        if (c == 'K')
            return WHITE_KING_SIDE;
        else if (c == 'k')
            return BLACK_KING_SIDE;
        else if (c == 'Q')
            return WHITE_QUEEN_SIDE;
        else if (c == 'q')
            return BLACK_QUEEN_SIDE;
        else return NONE;
    }
}
