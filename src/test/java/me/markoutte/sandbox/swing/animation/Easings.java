package me.markoutte.sandbox.swing.animation;

/**
 * Predefined animation curves
 *
 * More: http://easings.net/ru
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-07-17
 */
public enum Easings implements Easing {

    Linear {
        @Override
        public double calc(double t, double b, double c, double d) {
            return c * t / d + b;
        }
    },

    Quadratic {
        @Override
        public double calc(double t, double b, double c, double d) {
            t /= d;
            return c * t * t + b;
        }
    },

    EaseBack {
        @Override
        public double calc(double t, double b, double c, double d) {
            double v = 1.70158;
            double tt;
            return c * ((tt = t / d - 1) * tt * ((v + 1) * tt + v) + 1) + b;
        }
    }

}
