package me.markoutte.sandbox.competitions.codewars;

/**
 * Write a function, persistence, that takes in a positive parameter num and returns its multiplicative persistence, which is the number of times you must multiply the digits in num until you reach a single digit.
 *
 * For example:
 *
 * persistence(39) == 3 // because 3*9 = 27, 2*7 = 14, 1*4=4
 * // and 4 has only one digit
 *
 * persistence(999) == 4 // because 9*9*9 = 729, 7*2*9 = 126,
 * // 1*2*6 = 12, and finally 1*2 = 2
 *
 * persistence(4) == 0 // because 4 is already a one-digit number
 *
 * @link https://www.codewars.com/kata/55bf01e5a717a0d57e0000ec/train/java
 */
public class Persist {

    public static void main(String[] args) throws Exception {
        System.out.println(persistence(999));
    }

    public static int persistence(long n) {
        int persistence = 0;
        while (true) {
            String view = Long.toString(n);
            int[] digits = new int[view.length()];
            if (digits.length > 1) {
                persistence++;
            } else {
                break;
            }
            char[] chars = view.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char ch = chars[i];
                digits[i] = ch - '0';
            }
            n = 1;
            for (int digit : digits) {
                n *= digit;
            }
        }
        return persistence;
    }
}
