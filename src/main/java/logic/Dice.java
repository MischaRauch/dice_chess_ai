package logic;

public abstract class Dice {

    /**
     * Need to only return valid piece types. We need some way to check if the number returned is valid
     * @return dice number
     */
    public static int roll() {
        return (int) ((Math.random() * 6.0) + 1);
    }

    /**
     * @param n dice number rolled
     * @param possibleRolls array length 6 of all dice numbers
     * Example: during construction diceArr should be {1,2,3,4,5,6}
     * @return true if dice number rolled is not in dice number array
     */
    public static boolean isValid(int n, int[] possibleRolls) {
        //arr should be length 6
        for(int i=0;i<6;i++) {
            if(n==possibleRolls[i]) {
                return false;
            }
        }
        return true;
    }

}
