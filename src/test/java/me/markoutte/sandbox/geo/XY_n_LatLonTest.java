package me.markoutte.sandbox.geo;

import org.junit.Assert;
import org.junit.Test;

import java.awt.geom.Point2D;

import static java.lang.Math.toRadians;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-08-05
 */
public class XY_n_LatLonTest {

    @Test
    public void ll2xy() {
        Point2D saint_petersburg = new Point2D.Double(toRadians(59.967700001123575), toRadians(30.324245475479437));
        Point2D xy = XY_n_LatLon.ll2xy(saint_petersburg);
        Assert.assertEquals(xy.getX(), 6653612, 0.001);
        Assert.assertEquals(xy.getY(), 6350572, 0.001);
    }

    @Test
    public void xy2ll() {
        Point2D saint_petersburg = new Point2D.Double(6653612, 6350572);
        Point2D ll = XY_n_LatLon.xy2ll(saint_petersburg);
        Assert.assertEquals(ll.getX(), toRadians(59.967700001123575), 0.01);
        Assert.assertEquals(ll.getY(), toRadians(30.324245475479437), 0.01);
    }

}
