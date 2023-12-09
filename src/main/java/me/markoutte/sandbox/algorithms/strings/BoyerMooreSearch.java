package me.markoutte.sandbox.algorithms.strings;

import me.markoutte.sandbox.algorithms.maps.OpenTable;

public class BoyerMooreSearch implements StringSearch {

    @Override
    public int find(String p, String s) {
        OpenTable<Character, Integer> table = new OpenTable<>(p.length());
        for (int i = 0; i < p.length() - 1; i++) {
            table.put(p.charAt(i), i);
        }
        loop: for (int i = 0; i < s.length() - p.length() + 1;) {
            for (int j = p.length() - 1; j > -1; j--) {
                if (p.charAt(j) != s.charAt(i + j)) {
                    Integer move = table.get(s.charAt(i + j));
                    i += Math.max(1, j - (move == null ? p.length() : move));
                    continue loop;
                }
            }
            return i;
        }
        return -1;
    }
}
