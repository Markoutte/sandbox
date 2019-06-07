package me.markoutte.sandbox.swing;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Simple clock example
 */
public class LabelClock {

    public static void main(String[] args) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);

        Timer timer = new Timer(10, e -> label.setText(String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tN", System.currentTimeMillis())));
        timer.start();

        JFrame frame = SwingExample.inFrame("Clock", label);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
            }
        });
    }

}
