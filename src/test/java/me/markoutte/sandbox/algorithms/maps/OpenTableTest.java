package me.markoutte.sandbox.algorithms.maps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OpenTableTest {

    @Test
    public void a() {
        OpenTable<String, Integer> table = new OpenTable<>(100);
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
        int total = 100000;
        OpenTable<String, Double> table = new OpenTable<>(total);
        for (int i = 0; i < total; i++) {
            table.put(String.valueOf(i), (double) i);
            Assertions.assertEquals(i + 1, table.size());
        }
        for (int i = 0; i < total; i++) {
            Assertions.assertEquals(i, table.get(String.valueOf(i)));
        }
        for (int i = 0; i < total; i++) {
            Assertions.assertEquals(i, table.remove(String.valueOf(i)));
            Assertions.assertEquals(total - 1 - i, table.size());
        }
    }

    @Test
    public void badHash() {
        class Bad {

            private final int hash;

            Bad(int hash) {
                this.hash = hash;
            }

            @Override
            public int hashCode() {
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }

            @Override
            public String toString() {
                return "h: " + hash;
            }
        }

        OpenTable<Bad, Integer> table = new OpenTable<>(100);
        for (int i = 0; i < 10; i++) {
            table.put(new Bad(10), i);
        }
        for (int i = 0; i < 10; i++) {
            table.put(new Bad(5), i + 10);
        }
        Assertions.assertEquals(20, table.size());
        for (int i = 10; i < 20; i++) {
            Assertions.assertEquals(i, table.table[i].value + 10);
        }
        for (int i = 5; i < 10; i++) {
            Assertions.assertEquals(i + 5, table.table[i].value);
        }
        for (int i = 20; i < 25; i++) {
            Assertions.assertEquals(i - 5, table.table[i].value);
        }
    }
}
