package me.markoutte.sandbox.profiling;

import me.markoutte.sandbox.swing.SwingPerformance;
import me.markoutte.sandbox.swing.monsters.EventCounter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class TestTimer {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        var nanos = TimeUnit.MILLISECONDS.toNanos(1);
        var count = 1000;
        SwingPerformance.windowsTimerHack();

        for (Method method : TestTimer.class.getDeclaredMethods()) {
            if (method.isSynthetic()) continue;
            Class<?>[] types = method.getParameterTypes();
            if (types.length == 4 && int.class.equals(types[0]) && long.class.equals(types[1]) && EventCounter.class.equals(types[2]) && BiConsumer.class.equals(types[3])) {
                var counter = new EventCounter(count * 100);
                var time = System.currentTimeMillis();
                method.invoke(
                        null, count, nanos, counter, (BiConsumer<String, Long>) (name, end)
                                -> System.out.println(name + " " + counter.size() + " t:" + (end - time) + " ms")
                );
            }
        }
    }

    private static void parkNanos(int count, long nanos, @NotNull EventCounter counter, @NotNull BiConsumer<String, Long> runnable) {
        new Thread(() -> {
            for (int i = 0; i < count; i++) {
                LockSupport.parkNanos(nanos);
                counter.update();
            }
            runnable.accept("Park nanos", System.currentTimeMillis());
        }).start();
    }

    private static void scheduleAtFixedRate(int count, long nanos, @NotNull EventCounter counter, @NotNull BiConsumer<String, Long> runnable) {
        var service = Executors.newSingleThreadScheduledExecutor();
        var c = new AtomicInteger();
        service.scheduleAtFixedRate(() -> {
            counter.update();
            if (c.incrementAndGet() >= count) {
                service.shutdownNow();
                runnable.accept("Schedule at Fixed Rate", System.currentTimeMillis());
            }
        }, 0, nanos, TimeUnit.NANOSECONDS);
    }

    private static void scheduleOneByOne(int count, long nanos, @NotNull EventCounter counter, @NotNull BiConsumer<String, Long> runnable) {
        var service = Executors.newSingleThreadScheduledExecutor();
        var c = new AtomicInteger();
        service.schedule(new Runnable() {
            @Override
            public void run() {
                counter.update();
                if (c.incrementAndGet() < count) {
                    service.schedule(this, nanos, TimeUnit.NANOSECONDS);
                } else {
                    runnable.accept("Schedule One by One", System.currentTimeMillis());
                    service.shutdownNow();
                }
            }
        }, 0, TimeUnit.NANOSECONDS);
    }

    private static void swingTimer(int count, long nanos, @NotNull EventCounter counter, @NotNull BiConsumer<String, Long> runnable) {
        var c = new AtomicInteger();
        new Timer((int)Math.max(1, TimeUnit.NANOSECONDS.toMillis(nanos)), e -> {
            counter.update();
            if (c.incrementAndGet() >= count) {
                runnable.accept("Swing Timer", System.currentTimeMillis());
                ((Timer)e.getSource()).stop();
            }
        }).start();
        SwingUtilities.invokeLater(() -> {});
    }
}