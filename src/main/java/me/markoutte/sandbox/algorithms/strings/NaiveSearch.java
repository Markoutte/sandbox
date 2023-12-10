package me.markoutte.sandbox.algorithms.strings;

public class NaiveSearch implements StringSearch {

    @Override
    public int find(String p, String s) {
        for (int i = 0; i < s.length() - p.length(); i++) {
            if (equals(p, s, i, i + p.length())) {
                return i;
            }
        }
        return -1;
    }
}
