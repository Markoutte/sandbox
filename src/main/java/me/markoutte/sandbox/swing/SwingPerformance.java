package me.markoutte.sandbox.swing;

import me.markoutte.sandbox.swing.monsters.EventCounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SwingPerformance {

    private static int i = 0;

    public static void main(String[] args) {
        windowsTimerHack();

        final long start = System.nanoTime();
        EventCounter c = new EventCounter(10000);
        int testDuration = 20_000;
        int[] results = new int[testDuration];
        JFrame frame = new JFrame();
        frame.setTitle("Nothing here");
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                results[i++] = c.update();
                if (i == testDuration) {
                    frame.dispose();
                }
            }
        };
        frame.setContentPane(pane);
        frame.pack();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(pane::repaint, 0, 1, TimeUnit.MILLISECONDS);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("OS: " + System.getProperty("os.name"));
                System.out.println("Expected duration: " + i + " ms");
                System.out.println("Average event per second: " + Arrays.stream(results).sum() / i);
                System.out.println("Time spent: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " ms");
                service.shutdown();
            }
        });
        frame.setVisible(true);
    }

    // https://hazelcast.com/blog/locksupport-parknanos-under-the-hood-and-the-curious-case-of-parking-part-ii-windows/
    public static void windowsTimerHack() {
        final String os = System.getProperty("os.name");
        if (os != null && os.startsWith("Windows")) {
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException ignored) {
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }
}