package me.markoutte.sandbox.math;

import java.awt.*;
import java.awt.geom.Point2D;

import static java.lang.Math.*;
import static java.lang.Math.hypot;
import static java.lang.Math.pow;

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

        double seg2_line1_start = a1 * start2.x + b1 * start2.y + d1;
        double seg2_line1_end = a1 * end2.x + b1 * end2.y + d1;

        if (seg1_line2_start * seg1_line2_end >= 0 || seg2_line1_start * seg2_line1_end >= 0) {
            return null;
        }

        double u = seg1_line2_start / (seg1_line2_start - seg1_line2_end);

        return new Point((int) (start1.x + u * dir1.x), (int) (start1.y + u * dir1.y));
    }

    public static Point2D[] intersection(Point2D lineStart, Point2D lineEnd, Point2D circleCenter, double circleRadius) {

        double x1 = lineStart.getX() - circleCenter.getX();
        double y1 = lineStart.getY() - circleCenter.getY();
        double x2 = lineEnd.getX() - circleCenter.getX();
        double y2 = lineEnd.getY() - circleCenter.getY();
        double r = circleRadius;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double dr = hypot(dx, dy);
        double det = x1 * y2 - x2 * y1;

        double temp = pow(r, 2) * pow(dr, 2) - pow(det, 2);

        if (temp < 0) {
            return new Point[0];
        }

        double x3 = det * dy;
        double y3 = -1 * det * dx;

        if (temp == 0) {
            return new Point2D[]{new Point2D.Double(
                    x3 / pow(dr, 2) + circleCenter.getX(),
                    y3 / pow(dr, 2) + circleCenter.getY()
            )};
        }

        double x31 = (x3 + signum(dy) * dx * sqrt(temp)) / pow(dr, 2);
        double y31 = (y3 + abs(dy) * sqrt(temp)) / pow(dr , 2);
        double x32 = (x3 - signum(dy) * dx * sqrt(temp)) / pow(dr, 2);
        double y32 = (y3 - abs(dy) * sqrt(temp)) / pow(dr , 2);

        return new Point2D[] {
                new Point2D.Double(x31 + circleCenter.getX(), y31 + circleCenter.getY()),
                new Point2D.Double(x32 + circleCenter.getX(), y32 + circleCenter.getY()),
        };
    }

}
