package logic;

public class WhiteDice extends Dice {

    int[] possibleRolls; // should be length 6

    public WhiteDice(int[] possibleRolls) {
        this.possibleRolls = possibleRolls;
    }
}
