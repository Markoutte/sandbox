package me.markoutte.sandbox.algorithms.strings;

public class KnuthMorrisPrattSearch implements StringSearch {

    @Override
    public int find(String p, String s) {
        String c = p + "#" + s;
        int[] prefix = new int[c.length()];
        for (int i = 1; i < c.length(); i++) {
            int j = prefix[i - 1];
            while (j > 0 && c.charAt(i) != c.charAt(j)) {
                j = prefix[j - 1];
            }
            if (c.charAt(i) == c.charAt(j)) {
                prefix[i] = j + 1;
            }
            if (prefix[i] == p.length()) {
                return i - 2 * p.length();
            }
        }

        return -1;
    }
}
