package me.markoutte.sandbox.math;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.Assert.*;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-11-16
 */
public class IntervalsTest {

    @Test
    public void intersection() {
        Point p1 = Intervals.intersection(new Point(2, 1), new Point(7, 6), new Point(2, 5), new Point(6, 1));
        Assert.assertNotNull(p1);
        Assert.assertEquals(p1, new Point(4, 3));
        Point p2 = Intervals.intersection(new Point(7, 9), new Point(9, 1), new Point(2, 5), new Point(6, 1));
        Assert.assertNull(p2);

        Point t1 = Intervals.intersection(new Point(1871, 950), new Point(1923, 865), new Point(1910, 0), new Point(1910, 864));
        Point t2 = Intervals.intersection(new Point(1910, 0), new Point(1910, 864), new Point(1871, 950), new Point(1923, 865));
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void circlesAndLineNoIntersection() {
        Point2D startLine = new Point2D.Double(0, 2);
        Point2D endLine = new Point2D.Double(2, 4);
        Point2D circleCenter = new Point2D.Double(2, 2);
        double r = 1;
        Point2D[] intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        Assert.assertTrue(intersection.length == 0);
    }

    @Test
    public void circlesAndLineOneIntersection() {
        Point2D startLine = new Point2D.Double(3, 5);
        Point2D endLine = new Point2D.Double(3, 0);
        Point2D circleCenter = new Point2D.Double(2, 2);
        double r = 1;
        Point2D[] intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        Assert.assertTrue(intersection.length == 1);
        Assert.assertEquals(new Point2D.Double(3, 2), intersection[0]);
    }

    @Test
    public void circlesAndLineTwoIntersection() {
        Point2D startLine = new Point2D.Double(4, 4);
        Point2D endLine = new Point2D.Double(0, 0);
        Point2D circleCenter = new Point2D.Double(2, 2);
        double r = 1;
        Point2D[] intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        Assert.assertTrue(intersection.length == 2);
        Point2D.Double firstExpected = new Point2D.Double(Math.cos(Math.toRadians(45)) * r + circleCenter.getX(), Math.sin(Math.toRadians(45)) * r + circleCenter.getX());
        Assert.assertEquals(firstExpected.getX(), intersection[0].getX(), 0.000000000001);
        Assert.assertEquals(firstExpected.getY(), intersection[0].getY(), 0.000000000001);
        Point2D.Double secondExpected = new Point2D.Double(Math.cos(Math.toRadians(225)) * r + circleCenter.getX(), Math.sin(Math.toRadians(225)) * r + circleCenter.getX());
        Assert.assertEquals(secondExpected.getX(), intersection[1].getX(), 0.000000000001);
        Assert.assertEquals(secondExpected.getY(), intersection[1].getY(), 0.000000000001);
    }

}