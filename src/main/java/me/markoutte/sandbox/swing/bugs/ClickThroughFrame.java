package me.markoutte.sandbox.swing.bugs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-11-17
 */
public class ClickThroughFrame extends JFrame {

    public ClickThroughFrame() throws HeadlessException {
        final TestMouse mouse = new TestMouse();
        setTitle("Test click through");
        JPanel contentPane = new JPanel();
        JButton button = new JButton("Create frame");
        button.addActionListener(e -> {
            TestFrame frame = new TestFrame(ClickThroughFrame.this, mouse);
            frame.setTitle("Test " + new SimpleDateFormat().format(new Date()));
            frame.setPreferredSize(new Dimension(200, 200));
            frame.pack();
            frame.setVisible(true);
        });
        contentPane.add(button);
        setContentPane(contentPane);
    }

    public static void main(String[] args) {
        ClickThroughFrame frame = new ClickThroughFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640, 480));
        frame.pack();
        frame.setVisible(true);
    }

    private static final class TestFrame extends JDialog {
        public TestFrame(JFrame parent, TestMouse mouse) throws HeadlessException {
            super(parent);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            addMouseListener(mouse);
            addMouseMotionListener(mouse);
            addMouseWheelListener(mouse);
        }
    }

    private static final class TestMouse implements MouseListener, MouseMotionListener, MouseWheelListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            System.out.println(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            System.out.println(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            System.out.println(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            System.out.println(e);
        }
    }

}
