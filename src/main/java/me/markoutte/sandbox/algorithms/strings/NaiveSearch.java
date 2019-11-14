package me.markoutte.sandbox.algorithms.strings;

public class NaiveSearch implements StringSearch {

    @Override
    public int find(String p, String s) {
        failed: for (int i = 0; i < s.length() - p.length(); i++) {
            for (int j = 0; j < p.length(); j++) {
                if (s.charAt(i + j) != p.charAt(j)) {
                    continue failed;
                }
            }
            return i;
        }
        return -1;
    }
}
