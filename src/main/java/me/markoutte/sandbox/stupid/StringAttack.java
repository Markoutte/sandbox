package me.markoutte.sandbox.stupid;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class StringAttack {

    public static void main(String[] args) {
        Map<String, Void> s = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            String string = new String(new byte[i], StandardCharsets.UTF_8);
            s.put(string, null);
        }
        System.out.println(s.size());
    }

}
