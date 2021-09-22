package logic;

public class Move {

    int diceRoll;
    int startPos;
    int endPos;
    char piece;
    Boolean isLegal; //passed as Null from GUI, returned true if move can happen,

    public Move(int diceRoll, int startPos, int endPos, char piece, Boolean isLegal) {
        this.diceRoll = diceRoll;
        this.startPos = startPos;
        this.endPos = endPos;
        this.piece = piece;
        this.isLegal = isLegal;
    }
}
