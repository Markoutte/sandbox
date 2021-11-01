package me.markoutte.sandbox.profiling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This example shows why algorithms are useful.
 */
public class CountEvents {

    // enable -XX:TieredStopAtLevel=0
    public static void main(String[] args) throws IOException {
        var path = Paths.get("dir");
        var paths = generateNames("abcdefghij", 5)
                .map(path::resolve)
//                .peek(CountEvents::deletePath)
                .collect(Collectors.toList());
        var count = new int[paths.size()];
        Counter fps = new DequeCounter(TimeUnit.MILLISECONDS.toNanos(1000));
        long start = System.nanoTime();
        for (int counter = 0; counter < count.length; counter++) {
            count[counter] = fps.update();
            Files.createDirectories(paths.get(counter));
        }
        long spent = System.nanoTime() - start;
        //noinspection OptionalGetWithoutIsPresent
        System.out.println("Average count: " + (int) (Arrays.stream(count).average().getAsDouble()) + " op");
        System.out.println("Spent time: " + TimeUnit.NANOSECONDS.toMillis(spent) + " ms");
    }

    private interface Counter {
        int update();
    }

    private static class DequeCounter implements Counter {

        // usually ArrayList is used, but ArrayDeque is pretty good
        private final Deque<Long> collection = new ArrayDeque<>();
        private final long interval;

        private DequeCounter(long interval) {
            this.interval = interval;
        }

        @Override
        public int update() {
            return update(collection, System.nanoTime(), interval);
        }

        @SuppressWarnings("CommentedOutCode")
        public static int update(Deque<Long> events, long nanos, long interval) {
            events.add(nanos);
            // we can remove values like this
            events.removeIf(aTime -> aTime < nanos - interval);

//            while (events.peekFirst() < nanos - interval) {
//                events.removeFirst();
//            }
            return events.size();
        }
    }

    @SuppressWarnings("unused")
    private static class SimpleCounter implements Counter {

        private long times, prev = -1;
        private int counter, frames;
        private boolean flashed;
        private final long interval;

        private SimpleCounter(long interval) {
            this.interval = interval;
        }

        public int update() {
            var stop = System.nanoTime();
            if (prev < 0) {
                prev = stop;
            }
            times += stop - prev;
            counter++;
            if (times > interval) {
                frames = counter;
                counter = 0;
                times = 0;
                flashed = true;
            }
            if (!flashed) {
                frames = counter;
            }
            prev = stop;
            return frames;
        }

    }

    @SuppressWarnings("SameParameterValue")
    private static Stream<String> generateNames(String alphabet, int length) {
        var spliterator = new Spliterator<String>() {

            final char[] chars = alphabet.toCharArray();
            final int combinations = (int) Math.pow(chars.length, length);
            final char[] tmp = new char[length];
            int i = 0;

            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                if (i < combinations) {
                    int d = i;
                    for (int k = length - 1; k >= 0; k--) {
                        int r = d % chars.length;
                        d /= chars.length;
                        tmp[k] = chars[r];
                    }
                    i++;
                    action.accept(new String(tmp));
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<String> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return combinations;
            }

            @Override
            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.IMMUTABLE;
            }
        };

        return StreamSupport.stream(spliterator, true);
    }

    @SuppressWarnings("unused")
    private static void deletePath(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignore) {}
    }
}
