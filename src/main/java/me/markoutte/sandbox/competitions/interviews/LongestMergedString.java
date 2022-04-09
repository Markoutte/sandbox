package me.markoutte.sandbox.competitions.interviews;


/**
 * Find the longest string of only 1. You can remove single 0 to merge two sequences.
 */
public class LongestMergedString {

    public static void main(String[] args) {
        System.out.println(findLongest("1101110"));
        System.out.println(findLongest("101010101"));
        System.out.println(findLongest("11011101111"));
        System.out.println(findLongest("110111001111"));
    }

    public static int findLongest(String s) {
        return findLongest(s.toCharArray(), 1);
    }

    public static int findLongest(String s, int zerosCanBeDelete) {
        return findLongest(s.toCharArray(), zerosCanBeDelete);
    }

    public static int findLongest(char[] c, int zerosCanBeDeleted) {
        // 111101111
        int max = 0;
        // zero means that this cursor removed all available zeros
        int[] ignored = new int[zerosCanBeDeleted + 1];
        for (int i = 0; i < ignored.length; i++) {
            ignored[i] = i;
        }
        int[] counter = new int[ignored.length];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < counter.length; j++) {
                if (c[i] == '1') {
                    counter[j]++;
                } else { // c[i] == '0'
                    boolean canBeIgnored = i < c.length - 1 && c[i + 1] == '1' && ignored[j] != 0;
                    if (!canBeIgnored) {
                        if (max < counter[j]) {
                            max = counter[j];
                        }
                        counter[j] = 0;
                    }
                    ignored[j] = ((ignored[j] - 1) + ignored.length) % ignored.length;
                }
            }
        }
        for (int i : counter) {
            if (max < i) {
                max = i;
            }
        }
        return max;
    }
    
}
