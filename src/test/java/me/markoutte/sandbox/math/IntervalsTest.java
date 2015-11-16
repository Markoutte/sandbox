package me.markoutte.sandbox.math;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

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
    }

}