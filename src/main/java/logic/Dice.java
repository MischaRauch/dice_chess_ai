package logic;

public class Dice {

    /**
     * Need to only return valid piece types. We need some way to check if the number returned is valid
     * @return
     */
    public static int roll() {
        return (int) ((Math.random() * 6.0) + 1);
    }
}
