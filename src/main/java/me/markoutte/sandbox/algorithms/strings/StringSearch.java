package me.markoutte.sandbox.algorithms.strings;

public interface StringSearch {

    int find(String pattern, String string);

    default boolean equals(String pattern, String text, int from, int to) {
        for (int i = from; i < to; i++) {
            if (pattern.charAt(i - from) != text.charAt(i)) {
                return false;
            }
        }
        return true;
    }

}
