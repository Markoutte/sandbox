package me.markoutte.sandbox.algorithms.strings;

public class RabinKarpSearch implements StringSearch {

    @Override
    public int find(String pattern, String string) {
        int patternLength = pattern.length();
        int hs = stringHashCode(pattern, 0, patternLength);
        int stringLength = string.length();
        int st = stringHashCode(string, 0, patternLength);
        for (int i = 0; i < stringLength - patternLength; i++) {
            if (hs == st && equals(pattern, string, i, i + patternLength)) {
                return i;
            }
            st = st - string.charAt(i) + string.charAt(i + patternLength);
        }
        return -1;
    }

    private int stringHashCode(String string, int start, int end) {
        int result = 0;
        for (int i = start; i < end; i++) {
            result += string.charAt(i);
        }
        return result;
    }
}
