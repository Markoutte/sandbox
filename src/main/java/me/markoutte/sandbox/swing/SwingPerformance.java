package me.markoutte.sandbox.swing;

import me.markoutte.sandbox.swing.monsters.EventCounter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SwingPerformance {

    private static int i = 0;

    public static void main(String[] args) {
        final long start = System.nanoTime();
        EventCounter c = new EventCounter(10000);
        int testDuration = 20_000;
        int[] results = new int[testDuration];
        JFrame frame = SwingExample.inFrame("Nothing here", new JPanel());
        Timer timer = new Timer(1, e -> {
            results[i++] = c.update();
            if (i == testDuration) {
                frame.dispose();
            }
        });
        timer.start();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("OS: " + System.getProperty("os.name"));
                System.out.println("Expected duration: " + testDuration + " ms");
                System.out.println("Average event per second: " + Arrays.stream(results).sum() / results.length);
                System.out.println("Time spent: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " ms");
                timer.stop();
            }
        });
        frame.setVisible(true);
    }

}
