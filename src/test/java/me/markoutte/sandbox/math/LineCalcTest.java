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
            Point2D result = Intervals.dropPerpendicular(
                    entry.getKey().get(0),
                    entry.getKey().get(1),
                    entry.getKey().get(2)
            );
            Assert.assertEquals(entry.getValue().getX(), result.getX(), 0);
            Assert.assertEquals(entry.getValue().getY(), result.getY(), 0);
        }
    }


}
