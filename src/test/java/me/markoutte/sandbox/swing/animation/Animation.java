package me.markoutte.sandbox.swing.animation;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Animation class responses for calculation point of animation and starting doing it.
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-07-17
 */
class Animation implements ActionListener, AnimationHook {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final Timer timer = new Timer(1, null);
    private final JComponent comp;
    private final Rectangle startBounds;
    private List<Pair<Rectangle, Easing>> endBounds = new ArrayList<>();

    public final static int X = 0;
    public final static int Y = 1;
    public final static int W = 2;
    public final static int H = 3;

    private Queue<Integer>[] paths = new Queue[4];
    private final AtomicBoolean isRunning = new AtomicBoolean();

    private int duration = 100;
    private Easing easing = Easings.EaseBack;
    private int threshold = 5;

    public Animation(JComponent component, Rectangle startBounds) {
        comp = component;
        this.startBounds = startBounds;
        timer.addActionListener(this);
    }

    public Animation start() {
        if (isRunning.getAndSet(true)) {
            throw new RuntimeException("Animation is already started");
        }

        paths[X] = new LinkedList<>();
        paths[Y] = new LinkedList<>();
        paths[W] = new LinkedList<>();
        paths[H] = new LinkedList<>();

        Rectangle tmp = startBounds;
        for (Pair<Rectangle, Easing> animation : endBounds) {
            Rectangle bounds = animation.get1();
            Easing easing = animation.get2();
            paths[X].addAll(createQueue(tmp.x, bounds.x, duration / threshold / endBounds.size(), easing));
            paths[Y].addAll(createQueue(tmp.y, bounds.y, duration / threshold/ endBounds.size(), easing));
            paths[W].addAll(createQueue(tmp.width, bounds.width, duration / threshold / endBounds.size(), easing));
            paths[H].addAll(createQueue(tmp.height, bounds.height, duration / threshold / endBounds.size(), easing));
            tmp = bounds;
        }

        comp.setBounds(startBounds);
        timer.setDelay(threshold);
        timer.start();
        return this;
    }

    public void addBounds(Rectangle bounds) {
        endBounds.add(new Pair<>(bounds, easing));
    }

    public void addBounds(Rectangle bounds, Easing easing) {
        endBounds.add(new Pair<>(bounds, easing));
    }

    private static Queue<Integer> createQueue(int start, int end, int duration, Easing easing) {
        Queue<Integer> list = new LinkedList<>();
        int total = end - start;
        for (int i = 0; i < duration; i++) {
            list.add((int) Math.ceil(easing.calc(i, start, total, duration)));
        }
        return list;
    }

    public void stop() {
        timer.stop();
    }

    public void actionPerformed(ActionEvent e) {

        if (paths[X].isEmpty() && paths[Y].isEmpty() && paths[W].isEmpty() && paths[H].isEmpty()) {
            stop();
            latch.countDown();
        } else {
            int x = paths[X].poll();
            int y = paths[Y].poll();
            int width = paths[W].poll();
            int height = paths[H].poll();

            comp.setBounds(x, y, width, height);
            comp.revalidate();
            comp.repaint();
        }
    }

    public void get() throws InterruptedException {
        latch.await();
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setEasing(Easing easing) {
        this.easing = easing;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    private static final class Pair<T1, T2> {
        private final T1 first;
        private final T2 second;

        public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
            return new Pair<>(first, second);
        }

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 get1() {
            return first;
        }

        /**
         * @return второе значение
         */
        public T2 get2() {
            return second;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.first);
            hash = 79 * hash + Objects.hashCode(this.second);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            final Pair<?, ?> other = (Pair<?, ?>) obj;
            if (!Objects.equals(this.first, other.first)) {
                return false;
            }
            if (!Objects.equals(this.second, other.second)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return new StringBuilder().
                    append('<').
                    append(first == this ? "(this Pair)" : first).
                    append(',').append(' ').
                    append(second == this ? "(this Pair)" : second).
                    append('>').
                    toString();
        }
    }
}
