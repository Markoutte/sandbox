package me.markoutte.sandbox.competitions.codefights;

import java.util.Stack;

/**
 * Let's say that a string s is a Binary string, if it consists of only A and B characters, i.e. for each valid i s[i]='A' or s[i]='B'.
 *
 * Let's call a Binary string a Beautiful Binary string, if it is possible to connect pairs of equal letter with lines above the string so that no two lines intersect and each character is connected to exactly one other character.
 *
 * Given a Binary string, determine if it is Beautiful.
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-29
 */
public class CF29032016 {

    public static void main(String[] args) {
        System.out.println(beautiful_binary_strings_2("ABAB"));
        System.out.println(beautiful_binary_strings_2("AAABBAABBA"));
    }

    /**
     * General solution of problem
     */
    private static boolean beautiful_binary_strings(String b) {
        Stack<Character> s = new Stack<>();
        for (char c : b.toCharArray()) {
            if (!s.empty() && s.peek() == c) {
                s.pop();
            } else {
                s.push(c);
            }
        }
        return s.empty();
    }

    /**
     * Redesigned solution of {@link #beautiful_binary_strings(String)} for shorter code footprint
     */
    private static boolean beautiful_binary_strings_1(String b) {
        Stack s = new Stack();
        for (char c : b.toCharArray())
            if (s.empty() || !s.peek().equals(c)) s.push(c); else s.pop();
        return s.empty();
    }

    /**
     * The winner of problem by CuongVM (good solution)
     */
    private static boolean beautiful_binary_strings_2(String s) {
        while (s != (s = s.replaceAll("AA|BB", "")));
        return s.isEmpty();
    }

}
