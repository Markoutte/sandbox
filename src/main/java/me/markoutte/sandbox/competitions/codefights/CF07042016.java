package me.markoutte.sandbox.competitions.codefights;

import java.util.Arrays;

/**
 * Return a list of all numbers that have no more than n bits, such that exactly k of them are set to 1.
 *
 * Example
 *
 * For n = 4 and k = 1, the output should be
 * kBitsDigits(n, k) = [1,2,4,8].
 *
 * 110 = 12, which obviously has 1 < 4 number of bits, and the only bit is 1.
 * 210 = 102, which has 2 < 4 number of bits, with the first one equal to 1.
 * 410 = 1002, which has 3 < 4 number of bits, with the first one equal to 1.
 * 810 = 102, which has 3 < 4 number of bits, with the first one equal to 1.
 *
 * [input] integer n
 *
 * The maximum number of bits the number can have, 1 ≤ n ≤ 20.
 *
 * [input] integer k
 *
 * The number of bits equal to 1, 0 ≤ k ≤ 20.
 *
 * [output] array.integer
 *
 * The numbers with at most n bits k of which are set to 1 sorted in ascending order.
 *
 * by https://codefights.com/profile/ravst
 *
 * @see https://codefights.com/challenge/uwTrqGQj8iE3xeibW
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-04-07
 */
public class CF07042016 {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(kBitsDigits(4, 1)));
    }

    private static int[] kBitsDigits(int n, int k) {
        int[] r = {};
        for (int i = -1; ++i < 1 << n;) {
            if (Long.bitCount(i) == k)
                (r = Arrays.copyOf(r, r.length + 1))[r.length - 1] = i;
        }
        return r;
    }

}
