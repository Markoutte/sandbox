package me.markoutte.sandbox.algorithms.strings;

import me.markoutte.sandbox.algorithms.BalancedMean;
import me.markoutte.sandbox.algorithms.Mean;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

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
    public void runDefaultStress() throws IOException {
        runStress(System.out, string, string.length(), 10000);
    }

    @Test
    public void runAliceStress() throws IOException {
        final String text;
        try (var s = StringSearch.class.getClassLoader().getResourceAsStream("alice.txt")) {
            text = new String(s.readAllBytes());
        }
        Path path = Path.of("stringSearch.csv");
        try (var writer = new PrintStream(Files.newOutputStream(path, StandardOpenOption.CREATE))) {
            runStress(writer, text, 1000, 10000);
        }
    }

    @FunctionalInterface
    private interface ConsumerWithIO {

        void accept(Map<? extends StringSearch, ? extends Mean> stringSearchMeanMap) throws IOException;
    }

    public static void runStress(PrintStream writer, String text, int length, int runs) throws IOException {
        writer.append("No;N;J,RK;BM;KMP\n");
        for (int i = 1; i < length - 1; i++) {
            final var fi = i;
            stressTest(text, runs, i, new Random().nextInt(), stringSearchMeanMap -> {
                double[] means = new double[5];
                stringSearchMeanMap.forEach((stringSearch, mean) -> {
                    if (stringSearch instanceof JavaStringSearch) {
                        means[0] = mean.getMean();
                    }
                    if (stringSearch instanceof NaiveSearch) {
                        means[1] = mean.getMean();
                    }
                    if (stringSearch instanceof RabinKarpSearch) {
                        means[2] = mean.getMean();
                    }
                    if (stringSearch instanceof BoyerMooreSearch) {
                        means[3] = mean.getMean();
                    }
                    if (stringSearch instanceof KnuthMorrisPrattSearch) {
                        means[4] = mean.getMean();
                    }
                });
                writer.append("%d;%.2f;%.2f;%.2f;%.2f;%.2f\n".formatted(fi, means[0], means[1], means[2], means[3], means[4]));
                writer.flush();
            });
        }
    }

    public static void stressTest(String text, int runs, int length, int seedValue, ConsumerWithIO out) throws IOException {
        Random random = new Random(seedValue);
        if (length >= text.length()) {
            throw new IllegalArgumentException("Length " + length + " is greater than text's length");
        }

        var algorithms = Map.of(
                new JavaStringSearch(), new BalancedMean(),
                new NaiveSearch(), new BalancedMean(),
                new BoyerMooreSearch(), new BalancedMean(),
                new KnuthMorrisPrattSearch(), new BalancedMean(),
                new RabinKarpSearch(), new BalancedMean()
        );
        for (int i = 0; i < runs; i++) {
            final int start;
            final int end;
            if (length < 0) {
                start = random.nextInt(text.length() - 1);
                end = random.nextInt(start + 1, text.length());
            } else {
                start = random.nextInt(text.length() - 1 - length);
                end = random.nextInt(start + 1, start + 1 + length);
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
        out.accept(algorithms);
    }
}
