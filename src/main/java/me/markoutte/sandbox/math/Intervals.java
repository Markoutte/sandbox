package me.markoutte.sandbox.math;

import java.awt.*;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-11-16
 */
public class Intervals {

    /**
     * Находит пересечение двух отрезков.
     *
     * http://users.livejournal.com/_winnie/152327.html?page=1
     *
     * @param start1 Начало первого отрезка
     * @param end1 Конец первого отрезка
     * @param start2 Начало второго отрезка
     * @param end2 Конец второго отрезка
     * @return точка пересечения или {@code null}, если пересечения нет
     */
    public static Point intersection(Point start1, Point end1, Point start2, Point end2) {

        Point dir1 = new Point(end1.x - start1.x, end1.y - start1.y);
        Point dir2 = new Point(end2.x - start2.x, end2.y - start2.y);

        double a1 = -dir1.y;
        double b1 = +dir1.x;
        double d1 = -(a1 * start1.x + b1 * start1.y);

        double a2 = -dir2.y;
        double b2 = +dir2.x;
        double d2 = -(a2 * start2.x + b2 * start2.y);

        double seg1_line2_start = a2 * start1.x + b2 * start1.y + d2;
        double seg1_line2_end = a2 * end1.x + b2 * end1.y + d2;

        double seg2_line1_start = a1 * start2.x + b1 * start2.x + d1;
        double seg2_line1_end = a1 * end2.x + b1 * end2.y + d1;

        if (seg1_line2_start * seg1_line2_end >= 0 || seg2_line1_start * seg2_line1_end >= 0) {
            return null;
        }

        double u = seg1_line2_start / (seg1_line2_start - seg1_line2_end);

        return new Point((int) (start1.x + u * dir1.x), (int) (start1.y + u * dir1.y));
    }

}
