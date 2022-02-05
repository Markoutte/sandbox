package me.markoutte.sandbox.stupid;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SchedulerAtFixedRate {

    public static void main(String[] args) {
        // when is off then animations doesn't work as expected,
        // because one task can be scheduled for first 15 ms,
        // others will be started instantly.
//        SwingPerformance.windowsTimerHack();

        var service = Executors.newSingleThreadScheduledExecutor();
        var counter = new AtomicInteger();
        var count = 879;
        var period = TimeUnit.MICROSECONDS.toNanos(1000);
        var start = System.nanoTime();

//        service.scheduleAtFixedRate(() -> {
//            if (counter.incrementAndGet() < count) {
//                System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime()));
//            } else {
//                var end = System.nanoTime();
//                System.out.println(TimeUnit.NANOSECONDS.toMicros(end - start) + " us total");
//                System.out.printf("%.2f us/op%n", TimeUnit.NANOSECONDS.toMicros(end - start) / (double) counter.get());
//                service.shutdownNow();
//            }
//        }, 0, period, TimeUnit.NANOSECONDS);

        service.schedule(new Runnable() {
            private final AtomicLong triggerTimer = new AtomicLong(System.nanoTime());

            @Override
            public void run() {
                if (counter.incrementAndGet() < count) {
                    System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime()));
                    // try to find true delay, if it is negative then start task instantly
                    var delay = Math.max(triggerTimer.addAndGet(period) - System.nanoTime(), 0);
                    service.schedule(this, delay, TimeUnit.NANOSECONDS);
                } else {
                    var end = System.nanoTime();
                    System.out.println(TimeUnit.NANOSECONDS.toMicros(end - start) + " us total");
                    System.out.printf("%.2f us/op%n", TimeUnit.NANOSECONDS.toMicros(end - start) / (double) counter.get());
                    service.shutdownNow();
                }
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

}
