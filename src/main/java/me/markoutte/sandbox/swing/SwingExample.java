package me.markoutte.sandbox.swing;

import javax.swing.*;
import java.awt.*;

public abstract class SwingExample {

    public static JFrame inFrame(String title, JComponent contentPane) {
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(contentPane);
        frame.pack();

        return frame;
    }

    private SwingExample() {
    }
}
