package me.markoutte.sandbox.stupid;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Pelevin Maksim <maks.pelevin@oogis.ru>
 *
 * @since 2018/03/21
 */
public class ScheduledExecutorFuckups {
    
    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

        Runnable goodRunnable = () -> System.out.println("I am good");
        Runnable badRunnable = () -> {
            System.out.println("I am bad one");
            // it will halt this task only
            throw new RuntimeException("Exception");
        };

        service.scheduleAtFixedRate(goodRunnable, 1, 1, TimeUnit.SECONDS);
        service.scheduleAtFixedRate(badRunnable, 1, 1, TimeUnit.SECONDS);

        while (true);
    }
    
}
