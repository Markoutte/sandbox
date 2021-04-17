package me.markoutte.sandbox.swing.monsters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FpsCounter {

    private final List<Long> times = new ArrayList<>();

    public int update() {
        long current = System.nanoTime();
        long interval = TimeUnit.SECONDS.toNanos(1);
        times.add(current);
        times.removeIf(time -> time < current - interval);
        return times.size() - 1;
    }

    public int get() {
        return times.size();
    }
}
