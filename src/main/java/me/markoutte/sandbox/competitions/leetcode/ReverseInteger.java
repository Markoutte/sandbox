package me.markoutte.sandbox.competitions.leetcode;

/**
 * Given a signed 32-bit integer x, return x with its digits reversed.
 * If reversing x causes the value to go outside the signed 32-bit
 * integer range [-2^31, 2^31 - 1], then return 0.
 */
public class ReverseInteger {

    public static void main(String[] args) {
        System.out.println(reverse(1234006));
        System.out.println(reverse(1534236469));
        System.out.println(reverse(1563847412));
        assert reverse(123) == 321;
        assert reverse(-123) == -321;
        assert reverse(120) == 21;
    }

    private static final int[] POWERS_OF_10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    public static int reverse(int x) {
        // -2147483648, 2147483647
        boolean z = false;
        int v = x < 0 ? -x : x;
        int r = 0;
        for (int i = 9, j = 0; i >= 0; i--) {
            int d = v / POWERS_OF_10[i] % 10;
            z |= d != 0;
            if (z) try {
                r = Math.addExact(r, Math.multiplyExact(POWERS_OF_10[j++], d));
            } catch (ArithmeticException ae) {
                return 0;
            }
        }
        return x < 0 ? -r : r;
    }
}
