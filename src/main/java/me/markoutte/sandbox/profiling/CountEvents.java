package me.markoutte.sandbox.profiling;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * This example shows why algorithms are useful.
 */
@SuppressWarnings("CommentedOutCode")
public class CountEvents {

    public static int update(Collection<Long> events, long nanos, long interval) {
        events.add(nanos);
        // we can remove values like this
        events.removeIf(aTime -> aTime < nanos - interval);
//        // but it is faster than that
//        Iterator<Long> iterator = events.iterator();
//        while (iterator.hasNext()) {
//            Long aTime = iterator.next();
//            if (aTime < nanos - interval) {
//                iterator.remove();
//            } else {
//                break;
//            }
//        }
        return events.size();
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            long interval = TimeUnit.SECONDS.toNanos(1);
            int[] count = new int[(int) TimeUnit.MILLISECONDS.toNanos(1)];
            var counter = 0;
            long start = System.nanoTime();
            // usually ArrayList is used, but ArrayDeque is pretty good
            Collection<Long> collection = new ArrayList<>();//new ArrayDeque<>();
            while (counter < count.length) {
                LockSupport.parkNanos(1);
                count[counter++] = update(collection, System.nanoTime(), interval);
            }
            //noinspection OptionalGetWithoutIsPresent
            System.out.println("Avg count: " + Arrays.stream(count).average().getAsDouble());
            System.out.println("Time spent: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " ms");
            service.shutdown();
        });
    }
}
