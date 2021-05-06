package me.markoutte.sandbox.profiling;

import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This example shows why algorithms are useful.
 */
public class CountEvents {

    public static int update(Deque<Long> events, long nanos, long interval) {
        events.add(nanos);
        // we can remove values like this
//        events.removeIf(aTime -> aTime < nanos - interval);

        //noinspection ConstantConditions
        while (events.peekFirst() < nanos - interval) {
            events.removeFirst();
        }
        return events.size();
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        int total = 100_000;
        long interval = TimeUnit.MILLISECONDS.toNanos(100);
        int[] count = new int[total];
        // usually ArrayList is used, but ArrayDeque is pretty good
        Deque<Long> collection = new ArrayDeque<>();
        for (int counter = 0; counter < count.length; counter++) {
            count[counter] = update(collection, System.nanoTime(), interval);
            // https://shipilev.net/blog/2014/nanotrusting-nanotime/
            Blackhole.consumeCPU(1000);
        }
        long spent = System.nanoTime() - start;
        //noinspection OptionalGetWithoutIsPresent
        System.out.println("Average count: " + (int) (Arrays.stream(count).average().getAsDouble()) + " op");
        System.out.println("Spent time: " + TimeUnit.NANOSECONDS.toMillis(spent) + " ms");
    }
}
