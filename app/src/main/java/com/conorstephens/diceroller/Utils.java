package com.conorstephens.diceroller;

public class Utils {
    /**
     * Returns a random number
     * @param max The max number that will be returned, 1 is always the minimum
     * @return the random number
     */
    public static int rollDice(int max) {
        int range = (max - 1) + 1;
        int result = (int) (Math.random() * range) + 1;

        return result;
    }


    /**
     * Matches the index of the spinner to the corresponding dice.
     */
    public static int getDiceMax(int selectedPosition) {
        int max = 0;

        switch (selectedPosition) {
            case 0:
                max = 4;
                break;
            case 1:
                max = 6;
                break;
            case 2:
                max = 8;
                break;
            case 3:
                max = 10;
                break;
            case 4:
                max = 12;
                break;
            case 5:
                max = 20;
        }
        return max;
    }
}
