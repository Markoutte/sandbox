package me.markoutte.sandbox.competitions.leetcode;

import java.util.*;

public class LongestPalindromicSubstring {

    public static void main(String[] args) {
        System.out.println(longestPalindrome("bananas"));
    }
    
    // faster implementation
    public static String longestPalindrome(String s) {
        char[] chars = s.toCharArray();
        if (isPalindrome(chars, 0, chars.length)) {
            return s;
        }
        
        int li = 0, lj = 0;
        // if palindrome has odd length then i is an integer number (e.g. aba -> i = 1)
        // if palindrome has event length then i is a floating (e.g. abba -> i = 1.5)
        for (double i = 0.0; i < chars.length; i += 0.5) {
            double max = Math.min(i, chars.length - 1 - i);
            int j = 0;
            while (j <= max && chars[(int) Math.floor(i - j)] == chars[(int) Math.ceil(i + j)]) {
                j++;
            }
            int from = (int) Math.floor(i - (j - 1));
            int to = (int) Math.ceil(i + (j - 1));
            if (lj - li < to - from) {
                li = from;
                lj = to;
            }
            System.out.println(i + " " + j + " " + s.substring(from, to + 1));
        }
        return s.substring(li, lj + 1);
    }

    // naive implementation
    public static String longestPalindromeNaive(String s) {
        char[] chars = s.toCharArray();
        if (isPalindrome(chars, 0, chars.length)) {
            return s;
        }
        int li = 0, lj = 0;

        for (int i = 0; i < chars.length; i++) {
            for (int j = chars.length - 1; j >= 0; j--) {
                if (isPalindrome(chars, i, j + 1) && j - i > lj - li) {
                    li = i;
                    lj = j;
                }
            }
        }

        return new String(Arrays.copyOfRange(chars, li, lj + 1));
    }

    /**
     * Test is current substring is a palindrome. 
     * @param string full string
     * @param from index where substring starts 
     * @param toExclusive index where substring ends (exclusive)
     * @return true if a current substring is a palindrome
     */
    private static boolean isPalindrome(char[] string, int from, int toExclusive) {
        boolean isPalindrome = true;
        for (int i = 0; i < (toExclusive - from) / 2; i++) {
            if (string[from + i] != string[toExclusive - 1 - i]) {
                isPalindrome = false;
                break;
            }
        }
        return isPalindrome;
    }
}
