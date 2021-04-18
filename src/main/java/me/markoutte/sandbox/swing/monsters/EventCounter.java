package me.markoutte.sandbox.swing.monsters;

import java.util.concurrent.TimeUnit;

public final class EventCounter {

    private final int capacity;
    private final long[] times;
    private final long interval = TimeUnit.SECONDS.toNanos(1);
    private int head = 0, tail = 0;

    public EventCounter(int capacity) {
        this.capacity = capacity;
        this.times = new long[capacity];
    }

    public int update() {
        final int next = (head + 1) % capacity;
        if (next == tail) throw new RuntimeException("Out of capacity");
        long current = System.nanoTime();
        int size = size();
        times[next] = current;
        for (int i = 0; i < size; i++) {
            if (times[(tail + i) % capacity] < current - interval) {
                tail = (tail + 1) % capacity;
            } else {
                break;
            }
        }
        head = next;
        return size;
    }

    public int size() {
        return (head < tail) ? head + capacity - tail : head - tail;
    }
}
