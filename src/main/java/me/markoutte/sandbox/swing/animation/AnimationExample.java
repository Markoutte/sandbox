package me.markoutte.sandbox.swing.animation;

import me.markoutte.sandbox.swing.SwingExample;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-08-12
 */
public final class AnimationExample {

    public static void main(String[] args) {
        SwingExample.inFrame("Animation example", new ContentPane()).setVisible(true);
    }

    private static class ContentPane extends JPanel {

        public ContentPane() {
            super(null);

            JButton button = new JButton("Show me");
            add(button);

            addAncestorListener(new AncestorAdapter() {
                @Override
                public void ancestorAdded(AncestorEvent event) {
                    button.setBounds(generateBounds());
                }
            });

            button.addActionListener(e -> {
                AnimationBuilder.on(button, button.getBounds())
                        .setDuration(250)
                        .addBounds(generateBounds(), Easings.EaseBack)
                        .animate();
            });
        }

        private Rectangle generateBounds() {
            Container frame = SwingUtilities.getAncestorOfClass(JFrame.class, this);
            Rectangle size = frame.getBounds();

            int x = (int) (Math.random() * size.width / 2);
            int y = (int) (Math.random() * size.height / 2);
            int width = (int) Math.max(Math.random() * (size.width - x), 100);
            int height = (int) Math.max(Math.random() * (size.height - y), 24);
            return new Rectangle(x, y, width, height);
        }
    }

    private static class AncestorAdapter implements AncestorListener {

        @Override
        public void ancestorAdded(AncestorEvent event) {
        }

        @Override
        public void ancestorRemoved(AncestorEvent event) {
        }

        @Override
        public void ancestorMoved(AncestorEvent event) {
        }
    }
}
