package me.markoutte.sandbox.algorithms.strings;

public class RabinKarpSearch implements StringSearch {

    @Override
    public int find(String pattern, String string) {
        int hs = stringHashCode(pattern);
        int st = stringHashCode(string.substring(0, pattern.length()));
        for (int i = 0; i < string.length() - pattern.length(); i++) {
            if (hs == st && pattern.equals(string.substring(i, i + pattern.length()))) {
                return i;
            }
            st = st - string.charAt(i) + string.charAt(i + pattern.length());
        }
        return -1;
    }

    private int stringHashCode(String string) {
        int result = 0;
        for (int i = 0; i < string.length(); i++) {
            result += string.charAt(i);
        }
        return result;
    }
}
