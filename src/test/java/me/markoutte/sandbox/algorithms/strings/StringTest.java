package me.markoutte.sandbox.algorithms.strings;

import me.markoutte.sandbox.algorithms.Mean;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringTest {

    private static final String string =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis " +
                    "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequa" +
                    "t. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum do" +
                    "lore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proide" +
                    "nt, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    private static final String pattern = "minim";;
    private static final int result = 135;

    @Test
    public void javaStringSearch() {
        assertEquals(result, string.indexOf(pattern));
    }

    @Test
    public void naiveStringSearch() {
        assertEquals(result, new NaiveSearch().find(pattern, string));
    }

    @Test
    public void boyerMooreSearch() {
        assertEquals(result, new BoyerMooreSearch().find(pattern, string));
    }

    @Test
    public void knuthMorrisPrattSearch() {
        assertEquals(result, new KnuthMorrisPrattSearch().find(pattern, string));
    }

    @Test
    public void rabinKarpSearch() {
        assertEquals(result, new RabinKarpSearch().find(pattern, string));
    }

    @Test
    public void runDefaultStress() {
        stressTest(string, 100000, Integer.MAX_VALUE, new Random().nextInt());
    }

    @Test
    public void runAliceStress() throws IOException {
        final String text;
        try (var s = StringSearch.class.getClassLoader().getResourceAsStream("alice.txt")) {
            text = new String(s.readAllBytes());
        }
        // 275 is where Boyer Moore and KMP has similar time at my machine
        stressTest(text, 10000, 275, new Random().nextInt());
    }

    public static void stressTest(String text, int runs, int limit, int seedValue) {
        System.out.println("Runs: " + runs);
        System.out.println("Limit: " + limit);
        System.out.println("Seed: " + seedValue);
        Random random = new Random(seedValue);

        var algorithms = Map.of(
                new NaiveSearch(), new Mean(),
                new BoyerMooreSearch(), new Mean(),
                new KnuthMorrisPrattSearch(), new Mean(),
                new RabinKarpSearch(), new Mean()
        );
        for (int i = 0; i < runs; i++) {
            final int start = random.nextInt(text.length() - 1);
            final int end;
            if (limit < 0 || limit > Integer.MAX_VALUE - text.length()) {
                end = random.nextInt(start + 1, text.length());
            } else {
                end = random.nextInt(start + 1, Math.min(start + 1 + limit, text.length()));
            }
            final String pattern = text.substring(start, end);
            final int expected = text.indexOf(pattern);
            algorithms.forEach((stringSearch, mean) -> {
                long s = System.nanoTime();
                int actual = stringSearch.find(pattern, text);
                if (actual < 0) {
                    stringSearch.find(pattern, text);
                }
                assertEquals(expected, actual, () -> "Cannot find pattern " + pattern + " for " + stringSearch);
                long e = System.nanoTime();
                mean.add(e - s);
            });
        }
        algorithms.keySet().stream()
                .sorted(Comparator.comparing(o -> o.getClass().getSimpleName()))
                .forEach(stringSearch -> {
                    double mean = algorithms.get(stringSearch).getMean();
                    String name = stringSearch.getClass().getSimpleName();
                    System.out.printf("%s: ~%.1f ns\n", name, mean);
                });
    }
}
