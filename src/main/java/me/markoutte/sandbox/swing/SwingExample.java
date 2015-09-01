package me.markoutte.sandbox.swing;

import javax.swing.*;
import java.awt.*;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-08-21
 */
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
