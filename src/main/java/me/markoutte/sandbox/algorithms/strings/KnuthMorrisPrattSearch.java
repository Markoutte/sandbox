package me.markoutte.sandbox.algorithms.strings;

/**
 * Implementation from CLRS 4th edition, p. 978.
 *
 * Except a workaround for pi, where it has size = p.length() + 1
 * and pi[0] is never used (to start calculation from the 1).
 */
public class KnuthMorrisPrattSearch implements StringSearch {

    @Override
    public int find(String p, String s) {
        var pi = computePrefixFunction(p);
        var q = 0;
        for (int i = 0; i < s.length(); i++) {
            while (q > 0 && p.charAt(q) != s.charAt(i)) {
                q = pi[q];
            }
            if (p.charAt(q) == s.charAt(i)) {
                q = q + 1;
            }
            if (q == p.length()) {
                return i - p.length() + 1;
            }
        }

        return -1;
    }

    private int[] computePrefixFunction(String p) {
        int[] pi = new int[p.length() + 1];
        int k = 0;
        for (int q = 1; q < p.length(); q++) {
            while (k > 0 && p.charAt(k) != p.charAt(q)) {
                k = pi[k];
            }
            if (p.charAt(k) == p.charAt(q)) {
                k = k + 1;
            }
            pi[q + 1] = k;
        }
        return pi;
    }
}
