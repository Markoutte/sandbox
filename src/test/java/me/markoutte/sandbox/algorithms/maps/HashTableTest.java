package me.markoutte.sandbox.algorithms.maps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashTableTest {

    @Test
    public void a() {
        HashTable<String, Integer> table = new HashTable<>(100);
        table.put("hello", 1);
        Assertions.assertEquals(1, table.size());
        Assertions.assertEquals(1, table.get("hello"));
        table.put("hello", 2);
        Assertions.assertEquals(1, table.size());
        Assertions.assertEquals(2, table.get("hello"));
        table.put("world", 3);
        Assertions.assertEquals(2, table.size());
        Assertions.assertEquals(2, table.get("hello"));
        Assertions.assertEquals(3, table.get("world"));
        table.remove("hello");
        Assertions.assertEquals(1, table.size());
        table.remove("world");
        Assertions.assertEquals(0, table.size());
    }

    @Test
    public void b() {
        HashTable<Integer, Double> table = new HashTable<>(100);
        int total = 100000;
        for (int i = 0; i < total; i++) {
            table.put(i, (double) i);
            Assertions.assertEquals(i + 1, table.size());
        }
        for (int i = 0; i < total; i++) {
            Assertions.assertEquals(i, table.get(i));
        }
        for (int i = 0; i < total; i++) {
            Assertions.assertEquals(i, table.remove(i));
            Assertions.assertEquals(total - 1 - i, table.size());
        }
    }

}
