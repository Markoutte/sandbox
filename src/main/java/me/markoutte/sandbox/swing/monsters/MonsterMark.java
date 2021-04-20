package me.markoutte.sandbox.swing.monsters;

import me.markoutte.sandbox.swing.SwingExample;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MonsterMark {

    private static final Image[] SPRITES = Arrays.stream(Monsters.values())
            .map(monsters -> SpriteImage32.createOne(monsters.getBase64(), bytes -> {
                try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
                    return ImageIO.read(bais);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })).toArray(Image[]::new);

    public static void main(String[] args) {
        Painter painter = new Painter();
        painter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                painter.requestNew = 1000;
            }
        });
        JFrame w = SwingExample.inFrame("Monsters Swing", painter);
        w.setVisible(true);
        painter.requestNew = 1;
    }

    private static class Painter extends JComponent {
        private final EventCounter fps = new EventCounter(10000);
        private final List<SpriteImage32<Image>> cages = new ArrayList<>();
        private double counter = 0;
        private int requestNew = 0;
        private long time = System.nanoTime();
        private final long nanos = TimeUnit.SECONDS.toNanos(1);
        private final int px = 50; // pixels per second
        private final JLabel info = new JLabel();

        public Painter() {

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                    long current = System.nanoTime();
                    counter += (current - time) / (double) nanos * px;
                    if (counter > px * 10_000) {
                        counter %= px * 10_000;
                    }
                    time = current;
                    info.setText(String.format("%d monsters, fps = %d", cages.size(), fps.size()));
                    info.setBounds(5, 5, info.getPreferredSize().width, info.getPreferredSize().height);
                    repaint();
            }, 0, 500, TimeUnit.MICROSECONDS);

            setOpaque(true);
            setBackground(Color.WHITE);
            setLayout(null);
            add(info);

            info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            info.setOpaque(true);
            info.setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            Rectangle bounds = g2d.getClipBounds();
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, bounds.width, bounds.height);

            checkRequestAndAddIfNeeded();

            long s = Math.round(counter);
            for (SpriteImage32<Image> cage : cages) {
                Point p = cage.calcPoint(s, bounds.width, bounds.height, 1f);
                long frame = time % nanos / (nanos / cage.source.getWidth(null) * SpriteImage32.SIZE);
                int offset = (int) (frame * SpriteImage32.SIZE);
                g2d.drawImage(cage.source,
                        p.x, p.y, p.x + cage.width, p.y + cage.height,
                        offset, 0, offset + cage.width, cage.height,
                        null
                );
            }

            g2d.dispose();
            fps.update();
        }

        private static final Random R = new Random();

        private void checkRequestAndAddIfNeeded() {
            if (requestNew > 0) {
                while (requestNew-- > 0) {
                    cages.add(SpriteImage32.catchOne(SPRITES[R.nextInt(SPRITES.length)]));
                }
            }
        }
    }
}
