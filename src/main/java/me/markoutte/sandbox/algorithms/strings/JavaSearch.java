package me.markoutte.sandbox.algorithms.strings;

// -XX:+UnlockDiagnosticVMOptions -XX:DisableIntrinsic=_indexOfIL,_indexOfL
public class JavaSearch implements StringSearch {
    @Override
    public int find(String pattern, String string) {
        return string.indexOf(pattern);
    }
}
