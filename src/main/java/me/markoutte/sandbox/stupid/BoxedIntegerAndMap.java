package me.markoutte.sandbox.stupid;

import java.util.IdentityHashMap;
import java.util.Map;

public class BoxedIntegerAndMap {

    public static void main(String[] args) {
        Map<Integer, String> map = new IdentityHashMap<>();
        map.put(190190190, "One");
        map.put(190190190, "Two");
        if (map.size() != 1) {
            throw new RuntimeException("Map size is " + map.size())
            ;
        }
    }

}
