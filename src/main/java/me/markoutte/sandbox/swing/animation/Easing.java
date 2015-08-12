package me.markoutte.sandbox.swing.animation;

/**
 * Animation curves.
 *
 * http://easings.net/ru
 * http://gizma.com/easing
 * http://upshots.org/actionscript/jsas-understanding-easing
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-07-17
 */
public interface Easing {
    /**
     * Calculates current point value
     * @param t current time of animation
     * @param b start value
     * @param c total points count
     * @param d animation duration
     * @return calculated value
     */
    double calc(double t, double b, double c, double d);
}
