package me.markoutte.sandbox.competitions.codewars;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  Complete the solution so that it reverses all of the words within the string passed in.
 *
 *  Example:
 *  <code>
 *      ReverseWords.reverseWords("The greatest victory is that which requires no battle");
 *      // should return "battle no requires which that is victory greatest The"
 *  </code>
 */
public class ReverseWords {

    public static void main(String[] args) {
        System.out.println(ReverseWords.reverseWords("The greatest victory is that which requires no battle"));
    }

    private static String reverseWords(String str) {
        List<String> split = Arrays.asList(str.split(" "));
        Collections.reverse(split);
        return String.join(" ", split);
    }

}
