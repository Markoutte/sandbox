package me.markoutte.sandbox.math;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

import static java.lang.Math.*;
import static java.lang.Math.hypot;
import static java.lang.Math.pow;

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

    /**
     * This piece of code is end of my math with set of equations for equation of line.
     */
    public static Point2D dropPerpendicular(Point2D p1, Point2D p2, Point2D p3) {
        // When first 2 points are equals then no calculation is required (anyway it gives NaN next)
        if (Objects.equals(p1, p2)) {
            return new Point2D.Double(p1.getX(), p2.getY());
        }

        double Y = (p2.getY() - p1.getY());
        double X = (p2.getX() - p1.getX());
        double Z = (p3.getY() - p1.getY());

        double Y2 = pow(Y, 2);
        double X2 = pow(X, 2);

        double x = (Y2 * p1.getX() + X2 * p3.getX() + Z * X * Y) / (Y2 + X2);
        double y = (x - p1.getX()) * Y / X + p1.getY();
        if (Double.isNaN(y)) {
            y = (x - p3.getX()) * (-X) / Y + p3.getY();
        }

        return new Point2D.Double(x, y);
    }

    /**
     * <p>Computes intersection for line defined by two points and circle with center and radius.</p>
     *
     * @see <a href="http://mathworld.wolfram.com/Circle-LineIntersection.html">Circle-Line Intersection</a>
     */
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

        double incidence = pow(r, 2) * pow(dr, 2) - pow(det, 2);

        if (incidence < 0) {
            return new Point[0];
        }

        if (incidence == 0) {
            return new Point2D[]{new Point2D.Double(
                    (det * dy) / pow(dr, 2) + circleCenter.getX(),
                    (-1 * det * dx) / pow(dr, 2) + circleCenter.getY()
            )};
        }

        double x31 = ((det * dy) + (dy < 0 ? -1 : 1) * dx * sqrt(incidence)) / pow(dr, 2);
        double y31 = ((-1 * det * dx) + abs(dy) * sqrt(incidence)) / pow(dr , 2);
        double x32 = ((det * dy) - (dy < 0 ? -1 : 1) * dx * sqrt(incidence)) / pow(dr, 2);
        double y32 = ((-1 * det * dx) - abs(dy) * sqrt(incidence)) / pow(dr , 2);

        return new Point2D[] {
                new Point2D.Double(x31 + circleCenter.getX(), y31 + circleCenter.getY()),
                new Point2D.Double(x32 + circleCenter.getX(), y32 + circleCenter.getY()),
        };
    }

    public static boolean inRange(Point2D start, Point2D end, Point2D hit) {
        if (start.getX() == end.getX()) {  // special case
            return start.getY() < end.getY() ? (start.getY() <= hit.getY() && hit.getY() <= end.getY()) : (end.getY() <= hit.getY() && hit.getY() <= start.getY());
        }

        double m = (end.getY() - start.getY()) / (end.getX() - start.getX());
        double r1 = start.getX() + m * start.getY();
        double r2 = end.getX() + m * end.getY();
        double r = hit.getX() + m * hit.getY();
        return r1 < r2 ? (r1 <= r && r <= r2) : (r2 <= r && r <= r1);
    }

}
