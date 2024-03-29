package me.markoutte.sandbox.math;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Point2D;

import static me.markoutte.sandbox.math.Intervals.inRange;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalsTest {

    @Test
    public void intersection() {
        Point p1 = Intervals.intersection(new Point(2, 1), new Point(7, 6), new Point(2, 5), new Point(6, 1));
        assertNotNull(p1);
        assertEquals(p1, new Point(4, 3));
        Point p2 = Intervals.intersection(new Point(7, 9), new Point(9, 1), new Point(2, 5), new Point(6, 1));
        assertNull(p2);

        Point t1 = Intervals.intersection(new Point(1871, 950), new Point(1923, 865), new Point(1910, 0), new Point(1910, 864));
        Point t2 = Intervals.intersection(new Point(1910, 0), new Point(1910, 864), new Point(1871, 950), new Point(1923, 865));
        assertEquals(t1, t2);
    }

    @Test
    public void circlesAndLineNoIntersection() {
        Point2D startLine = new Point2D.Double(0, 2);
        Point2D endLine = new Point2D.Double(2, 4);
        Point2D circleCenter = new Point2D.Double(2, 2);
        double r = 1;
        Point2D[] intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        assertEquals(0, intersection.length);
    }

    @Test
    public void circlesAndLineOneIntersection() {
        Point2D startLine = new Point2D.Double(3, 5);
        Point2D endLine = new Point2D.Double(3, 0);
        Point2D circleCenter = new Point2D.Double(2, 2);
        double r = 1;
        Point2D[] intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        assertEquals(1, intersection.length);
        assertEquals(new Point2D.Double(3, 2), intersection[0]);
        assertTrue(inRange(startLine, endLine, intersection[0]));

        startLine = new Point2D.Double(0, 1);
        endLine = new Point2D.Double(3, 1);
        intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        assertEquals(1, intersection.length);
        assertEquals(new Point2D.Double(2, 1), intersection[0]);
        assertTrue(inRange(startLine, endLine, intersection[0]));
    }

    @Test
    public void circlesAndLineTwoIntersection() {
        Point2D startLine = new Point2D.Double(4, 4);
        Point2D endLine = new Point2D.Double(0, 0);
        Point2D circleCenter = new Point2D.Double(2, 2);
        double r = 1;
        Point2D[] intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        assertEquals(2, intersection.length);
        Point2D.Double firstExpected = new Point2D.Double(Math.cos(Math.toRadians(45)) * r + circleCenter.getX(), Math.sin(Math.toRadians(45)) * r + circleCenter.getX());
        assertEquals(firstExpected.getX(), intersection[0].getX(), 0.000000000001);
        assertEquals(firstExpected.getY(), intersection[0].getY(), 0.000000000001);
        Point2D.Double secondExpected = new Point2D.Double(Math.cos(Math.toRadians(225)) * r + circleCenter.getX(), Math.sin(Math.toRadians(225)) * r + circleCenter.getX());
        assertEquals(secondExpected.getX(), intersection[1].getX(), 0.000000000001);
        assertEquals(secondExpected.getY(), intersection[1].getY(), 0.000000000001);

        assertTrue(inRange(startLine, endLine, intersection[0]));
        assertTrue(inRange(startLine, endLine, intersection[1]));
    }

    @Test
    public void circleAndLineError() {
        double r = 1;
        Point2D startLine = new Point2D.Double(0, 4);
        Point2D endLine = new Point2D.Double(4, 4);
        Point2D circleCenter = new Point2D.Double(8, 4);
        Point2D[] intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        assertFalse(inRange(startLine, endLine, intersection[0]));
        assertFalse(inRange(startLine, endLine, intersection[1]));

        startLine = new Point2D.Double(0, 0);
        endLine = new Point2D.Double(0, 4);
        circleCenter = new Point2D.Double(0, 8);
        intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        assertFalse(inRange(startLine, endLine, intersection[0]));
        assertFalse(inRange(startLine, endLine, intersection[1]));

        startLine = new Point2D.Double(0, 4);
        endLine = new Point2D.Double(4, 4);
        circleCenter = new Point2D.Double(5, 4);
        intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        assertEquals(2, intersection.length);
        assertEquals(new Point2D.Double(6, 4), intersection[0]);
        assertEquals(new Point2D.Double(4, 4), intersection[1]);
        assertFalse(inRange(startLine, endLine, intersection[0]));
        assertTrue(inRange(startLine, endLine, intersection[1]));

        startLine = new Point2D.Double(0, 0);
        endLine = new Point2D.Double(0, 4);
        circleCenter = new Point2D.Double(0, 5);
        intersection = Intervals.intersection(startLine, endLine, circleCenter, r);
        assertEquals(2, intersection.length);
        assertEquals(new Point2D.Double(0, 6), intersection[0]);
        assertEquals(new Point2D.Double(0, 4), intersection[1]);
        assertFalse(inRange(startLine, endLine, intersection[0]));
        assertTrue(inRange(startLine, endLine, intersection[1]));
    }

}