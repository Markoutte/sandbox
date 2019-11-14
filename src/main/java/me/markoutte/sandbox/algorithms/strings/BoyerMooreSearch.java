package me.markoutte.sandbox.algorithms.strings;

import java.util.HashMap;
import java.util.Map;

public class BoyerMooreSearch implements StringSearch {

    @Override
    public int find(String p, String s) {
        Map<Character, Integer> table = new HashMap<>(p.length());
        for (int i = 0; i < p.length() - 1; i++) {
            table.put(p.charAt(i), p.length() - i - 1);
        }
        loop: for (int i = 0; i < s.length() - p.length() + 1;) {
            for (int j = p.length() - 1; j > -1; j--) {
                if (p.charAt(j) != s.charAt(i + j)) {
                    Integer move = table.get(s.charAt(i + j));
                    i += move == null ? p.length() : move - (p.length() - 1 - j);
                    continue loop;
                }
            }
            return i;
        }
        return -1;
    }
}
