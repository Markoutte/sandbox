package me.markoutte.sandbox.algorithms.strings;

public class JavaStringSearch implements StringSearch {
    @Override
    public int find(String pattern, String string) {
        return string.indexOf(pattern);
    }
}
