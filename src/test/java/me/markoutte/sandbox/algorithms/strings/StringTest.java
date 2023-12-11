package me.markoutte.sandbox.algorithms.strings;

import me.markoutte.sandbox.algorithms.BalancedMean;
import me.markoutte.sandbox.algorithms.Mean;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    private static final int maxPatternLength = 1000;
    private static final int patternLength = 256;
    private static final int maxTextLength = 5000;
    private static final int runs = 10_000;

    @Test
    public void runDefaultStress() {
        runVariablePatternLengthStressTest(string, string.length(), runs, System.out);
//        runVariableTextLengthStressTest(string, string.length(), patternLength, runs, System.out);
    }

    @Test
    public void runCodeStress() throws IOException {
        final StringBuilder sb = new StringBuilder();
        Files.walk(Paths.get("src"))
                .filter(path -> path.toString().endsWith(".java") || path.toString().endsWith(".kt"))
                .forEach(path -> {
                    try {
                        sb.append(Files.readString(path));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        final String text = sb.toString();
        Path path = Path.of("string-code.csv");
        try (var writer = new PrintStream(Files.newOutputStream(path, StandardOpenOption.CREATE))) {
//            runVariablePatternLengthStressTest(text, maxPatternLength, runs, writer);
            runVariableTextLengthStressTest(text, maxTextLength, patternLength, runs, writer);
        }
    }

    @Test
    public void runDnaStress() throws IOException {
        final String text;
        try (var s = StringSearch.class.getClassLoader().getResourceAsStream("dna.txt")) {
            text = new String(s.readAllBytes());
        }
        Path path = Path.of("string-dna.csv");
        try (var writer = new PrintStream(Files.newOutputStream(path, StandardOpenOption.CREATE))) {
            runVariablePatternLengthStressTest(text, maxPatternLength, runs, writer);
//            runVariableTextLengthStressTest(text, maxTextLength, patternLength, runs, writer);
        }
    }

    @Test
    public void runAliceStress() throws IOException {
        final String text;
        try (var s = StringSearch.class.getClassLoader().getResourceAsStream("alice.txt")) {
            text = new String(s.readAllBytes());
        }
        Path path = Path.of("string-alice.csv");
        try (var writer = new PrintStream(Files.newOutputStream(path, StandardOpenOption.CREATE))) {
//            runVariablePatternLengthStressTest(text, maxPatternLength, runs, writer);
            runVariableTextLengthStressTest(text, maxTextLength, patternLength, runs, writer);
        }
    }

    @FunctionalInterface
    private interface ConsumerWithIO {

        void accept(Map<? extends StringSearch, ? extends Mean> stringSearchMeanMap) throws IOException;
    }

    public static void runVariableTextLengthStressTest(String text, int maxTextLength, int patternLength, int runs, PrintStream writer) {
        writer.append("No;J;N;RK;BM;KMP\n");
        for (int textLength = patternLength + 1; textLength < maxTextLength; textLength++) {
            final var fi = textLength;
            stressTest(text.substring(0, textLength), patternLength, runs, new Random().nextInt(), stringSearchMeanMap -> {
                double[] means = new double[5];
                stringSearchMeanMap.forEach((stringSearch, mean) -> {
                    if (stringSearch instanceof JavaSearch) {
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

    public static void runVariablePatternLengthStressTest(String text, int maxPatternLength, int runs, PrintStream writer) {
        writer.append("No;J;N;RK;BM;KMP\n");
        for (int patternLength = 1; patternLength < maxPatternLength; patternLength++) {
            final var fi = patternLength;
            stressTest(text, patternLength, runs, new Random().nextInt(), stringSearchMeanMap -> {
                double[] means = new double[5];
                stringSearchMeanMap.forEach((stringSearch, mean) -> {
                    if (stringSearch instanceof JavaSearch) {
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

    /**
     * Runs stress test for a given pattern's length.
     * <p>
     * If pattern length <= 0 then random length is used. Random is seeded by seedValue.
     * This method cuts pattern with the given length from random place in the source text
     * and then runs search algorithms:
     *
     * <ul>
     *     <li>{@link JavaSearch} is Java's implementation that uses {@link String#indexOf(String)}</li>
     *     <li>{@link NaiveSearch} is a naive search implementation</li>
     *     <li>{@link RabinKarpSearch}</li>
     *     <li>{@link BoyerMooreSearch}</li>
     *     <li>{@link KnuthMorrisPrattSearch}</li>
     * </ul>
     *
     * @param text A text for search.
     * @param patternLength Given length of a pattern to search in or -1 for random length. Pattern is generated from the text.
     * @param runs The total number of calling every implementation of {@link StringSearch}
     * @param seedValue this value is used to seed Random
     * @param out consumes the result of one run
     */
    private static void stressTest(String text, int patternLength, int runs, int seedValue, ConsumerWithIO out) {
        Random random = new Random(seedValue);
        if (patternLength > text.length()) {
            throw new IllegalArgumentException("Length " + patternLength + " is greater than text's length");
        }

        var algorithms = Map.of(
                new JavaSearch(), new BalancedMean(),
                new NaiveSearch(), new BalancedMean(),
                new BoyerMooreSearch(), new BalancedMean(),
                new KnuthMorrisPrattSearch(), new BalancedMean(),
                new RabinKarpSearch(), new BalancedMean()
        );
        for (int i = 0; i < runs; i++) {
            final int start;
            final int end;
            if (patternLength <= 0) {
                start = random.nextInt(text.length() - 1);
                end = random.nextInt(start + 1, text.length());
            } else {
                int bound = text.length() - patternLength + 1;
                start = random.nextInt(bound);
                end = start + patternLength;
            }
            final String pattern = text.substring(start, end);
            final int expected = text.indexOf(pattern);
            algorithms.forEach((stringSearch, mean) -> {
                long s = System.nanoTime();
                int actual = stringSearch.find(pattern, text);
                assertEquals(expected, actual, () -> "Cannot find pattern " + pattern + " for " + stringSearch.getClass().getSimpleName());
                long e = System.nanoTime();
                mean.add(e - s);
            });
        }
        try {
            out.accept(algorithms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
