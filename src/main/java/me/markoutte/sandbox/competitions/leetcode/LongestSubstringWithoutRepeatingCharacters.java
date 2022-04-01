package me.markoutte.sandbox.competitions.leetcode;

import java.util.*;

/**
 * Given a string s, find the length of the longest substring without repeating characters.
 */
public class LongestSubstringWithoutRepeatingCharacters {

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring(" "));
    }

    public static int lengthOfLongestSubstring(String s) {
        var chars = s.toCharArray();
        int max = 0;
        for (int i = 0; i < chars.length; i++) {
            HashSet<Character> set = new HashSet<>();
            for (int j = i; j < chars.length; j++) {
                if (!set.add(chars[j])) {
                    break;
                }
            }
            if (max < set.size()) {
                max = set.size();
            }
        }
        return max;
    }
    
}
