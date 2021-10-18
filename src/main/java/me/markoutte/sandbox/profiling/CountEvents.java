package me.markoutte.sandbox.profiling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This example shows why algorithms are useful.
 */
public class CountEvents {

    // enable -XX:TieredStopAtLevel=0
    public static void main(String[] args) throws IOException {
        var path = Paths.get("dir");
        var allFolders = new ArrayList<Path>(100_000);
        String[] subFolders = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        addFoldersRecursively(path, subFolders, 5, allFolders);
        int[] count = new int[allFolders.size()];
        long start = System.nanoTime();
        Counter fps = new DequeCounter(TimeUnit.MILLISECONDS.toNanos(1000));
        for (int counter = 0; counter < count.length; counter++) {
            count[counter] = fps.update();
            Files.createDirectories(allFolders.get(counter));
        }
        long spent = System.nanoTime() - start;
        //noinspection OptionalGetWithoutIsPresent
        System.out.println("Average count: " + (int) (Arrays.stream(count).average().getAsDouble()) + " op");
        System.out.println("Spent time: " + TimeUnit.NANOSECONDS.toMillis(spent) + " ms");
    }

    @SuppressWarnings({"RedundantThrows", "CommentedOutCode"})
    private static void addFoldersRecursively(Path parent, String[] folders, int level, List<Path> result) throws IOException {
        if (level < 0) {
            throw new IllegalArgumentException();
        }
        if (level == 0) {
            result.add(parent);
            return;
        }
        for (String folder : folders) {
            var path = parent.resolve(folder);
            addFoldersRecursively(path, folders, level - 1, result);
//            if (Files.exists(path)) {
//                Files.delete(path);
//            }
        }
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
