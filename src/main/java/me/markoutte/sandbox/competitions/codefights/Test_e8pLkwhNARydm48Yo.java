package me.markoutte.sandbox.competitions.codefights;

import java.util.Arrays;

/**
 * You are given a decimal number n as a string. Transform it into an array of numbers (given as strings again), such that each number has only one nonzero digit and their sum equals n.
 *
 * Each number in the output array should be written without any leading and trailing zeros.
 *
 * Example
 *
 * For n = "7970521.5544" the output should be
 * exp(n) = ["7000000", "900000", "70000", "500", "20", "1", ".5", ".05", ".004", ".0004"]
 *
 * For n = "7496314" the output should be
 * exp(n) = ["7000000", "400000", "90000", "6000", "300", "10", "4"]
 *
 * exp("0") = []
 *
 * [input] string n
 *
 * 1 ≤ n.length ≤ 13.
 * [output] array.string
 *
 * Elements in the array should be sorted in descending order.
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-02-04
 */
public class Test_e8pLkwhNARydm48Yo {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(exp("7970521.5544")));
        System.out.println(Arrays.toString(exp("7496314")));
        System.out.println(Arrays.toString(exp("0")));
        System.out.println(Arrays.toString(exp("6")));
        System.out.println(Arrays.toString(exp(".630320")));
    }

    private static String[] exp(String n) {
        String[] r = new String[n.replaceAll("[0,.]", "").length()];
        int d = n.indexOf(".");
        d = d < 0 ? n.length() : d;
        for (int i = 0, j = 0; i < n.length(); i++) {
            char c = n.charAt(i);
            if (!"0.".contains("" + c)) {
                String p = "" + (c - 48) * (int) Math.pow(10, Math.abs(d - i) - 1);
                r[j++] = i < d ? p : "." + new StringBuilder(p).reverse();
            }
        }
        return r;
    }

}
