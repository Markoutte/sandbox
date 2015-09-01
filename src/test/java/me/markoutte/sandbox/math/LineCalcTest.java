package me.markoutte.sandbox.math;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.*;

import static java.lang.Math.*;

/**
 * Right the math, that calculates two lines intersection with 3 points known.
 *
 * First 2 points describe line and from 3 point required to let fall a perpendicular upon a line.
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-09-01
 */
public class LineCalcTest {

    private List<Point2D> set1 = Arrays.asList(
            new Point2D.Double(3, 8),
            new Point2D.Double(9, 2),
            new Point2D.Double(1, 1)
    );

    private List<Point2D> set2 = Arrays.asList(
            new Point2D.Double(5, 5),
            new Point2D.Double(5, 0),
            new Point2D.Double(0, 0)
    );

    private List<Point2D> set3 = Arrays.asList(
            new Point2D.Double(0, 5),
            new Point2D.Double(5, 5),
            new Point2D.Double(5, 0)
    );

    private List<Point2D> set4 = Arrays.asList(
            new Point2D.Double(4, 3),
            new Point2D.Double(4, 3),
            new Point2D.Double(4, 3)
    );

    private List<Point2D> set5 = Arrays.asList(
            new Point2D.Double(2, 2),
            new Point2D.Double(2, 2),
            new Point2D.Double(4, 3)
    );

    private List<Point2D> set6 = Arrays.asList(
            new Point2D.Double(4, 3),
            new Point2D.Double(2, 2),
            new Point2D.Double(2, 2)
    );

    private Map<List<Point2D>, Point2D> hugeSet = new LinkedHashMap<>();

    @Before
    public void setUp() {
        hugeSet.put(set1, new Point2D.Double(5.5, 5.5));
        hugeSet.put(set2, new Point2D.Double(5, 0));
        hugeSet.put(set3, new Point2D.Double(5, 5));
        hugeSet.put(set4, new Point2D.Double(4, 3));
        hugeSet.put(set5, new Point2D.Double(2, 2));
        hugeSet.put(set6, new Point2D.Double(2, 2));
    }

    @Test
    public void run() {

        for (Map.Entry<List<Point2D>, Point2D> entry : hugeSet.entrySet()) {
            Point2D result = calc(
                    entry.getKey().get(0),
                    entry.getKey().get(1),
                    entry.getKey().get(2)
            );
            Assert.assertEquals(entry.getValue().getX(), result.getX(), 0);
            Assert.assertEquals(entry.getValue().getY(), result.getY(), 0);
        }
    }

    /**
     * This piece of code is end of my math with set of equations for equation of line.
     */
    public static Point2D calc(Point2D p1, Point2D p2, Point2D p3) {

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

}
