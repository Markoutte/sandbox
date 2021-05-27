package me.markoutte.sandbox.profiling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * This example shows why algorithms are useful.
 */
public class CountEvents {

    // enable -XX:TieredStopAtLevel=0
    public static void main(String[] args) throws IOException {
        var path = Paths.get("dir");
        long start = System.nanoTime();
        int total = 100_000;
        long interval = TimeUnit.MILLISECONDS.toNanos(100);
        int[] count = new int[total];
        Counter fps = new DequeCounter(interval);
        for (int counter = 0; counter < count.length; counter++) {
            count[counter] = fps.update();
//            if (!Files.exists(path)) {
                Files.createDirectories(path);
//            }
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
}
