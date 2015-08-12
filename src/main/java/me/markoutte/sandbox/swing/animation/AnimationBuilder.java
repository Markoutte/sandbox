package me.markoutte.sandbox.swing.animation;

import javax.swing.*;
import java.awt.*;

/**
 * This builder is easy way to correctly initialize animation for certain {@link JComponent}.
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-07-17
 */
public final class AnimationBuilder {

    private final Animation animation;

    private AnimationBuilder(JComponent animated, Rectangle startBounds) {
        animation = new Animation(animated, startBounds);
    }

    /**
     * Creates new builder on certain component, that will be animated.
     *
     * The component should be places on JLayeredPane or on component, that has null layout manager.
     *
     * @param component JLayeredPane or JComponent with layout manager == null
     * @param startBounds initial bound of component
     *
     * @throws AssertionError if conditions are not right.
     */
    public static AnimationBuilder on(JComponent component, Rectangle startBounds) {
        Container parent = component.getParent();
        if (!(parent instanceof JLayeredPane) && (parent.getLayout() != null)) {
            throw new AssertionError("Component must be placed to JLayeredPane or on component with layout == null");
        }

        return new AnimationBuilder(component, startBounds);
    }

    /**
     * Duration of animation in milliseconds.
     */
    public AnimationBuilder setDuration(int duration) {
        animation.setDuration(duration);
        return this;
    }

    /**
     * The easing of animation.
     *
     * @see Easings predefined easing
     */
    public AnimationBuilder setEasing(Easing easing) {
        animation.setEasing(easing);
        return this;
    }

    /**
     * @param threshold trims count of way points for more smooth animations in some systems. Default is 5.
     * @throws IllegalArgumentException if value less than 1
     */
    public AnimationBuilder setThreashold(int threshold) {
        if (threshold < 1) {
            throw new IllegalArgumentException("threshold cannot be less then 1");
        }
        animation.setThreshold(threshold);
        return this;
    }

    /**
     * Adds new point to animation. Animation same as {@link #setEasing(Easing)} used or default.
     */
    public AnimationBuilder addBounds(Rectangle bounds) {
        animation.addBounds(bounds);
        return this;
    }

    /**
     * Adds new point to animation.
     */
    public AnimationBuilder addBounds(Rectangle bounds, Easing easing) {
        animation.addBounds(bounds, easing);
        return this;
    }

    /**
     * Start animation and returns hook to hold on waiting, while animation is not done.
     * @return The hook, that used for waiting, while animation in progress
     */
    public AnimationHook animate() {
        return animation.start();
    }
}