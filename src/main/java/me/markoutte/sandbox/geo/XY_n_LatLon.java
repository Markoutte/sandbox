package me.markoutte.sandbox.geo;

import java.awt.geom.Point2D;

import static java.lang.Math.*;

/**
 * This class describes the function to transform data from XY to LatLon and vice versa,
 * standing on GOST 51794-2008.
 *
 * @link http://protect.gost.ru/v.aspx?control=8&baseC=-1&page=0&month=-1&year=-1&search=&RegNum=1&DocOnPageCount=15&id=166620&pageK=49AC208C-DE64-46EE-BB57-95DDB08054BB
 *
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-08-05
 */
public class XY_n_LatLon {

    public static void main(/*ignored*/ String[] args) {
        Point2D saint_petersburg = new Point2D.Double(toRadians(59.967700001123575), toRadians(30.324245475479437));
        Point2D xy = ll2xy(saint_petersburg);
        System.out.println(xy);
//        Coordinate ll = xy2ll(new Coordinate(6653612, 6350572, 0.0));
        Point2D ll = xy2ll(xy);
        System.out.println(toDegrees(ll.getX()) + ", " + toDegrees(ll.getY()));
    }

    private static Point2D ll2xy(Point2D source) {
        double B = source.getX();
        double L = toDegrees(source.getY());

        int n = (int) floor((6 + L) / 6);
        double l = (L - abs(3 + 6 * (n - 1))) / 57.29577951;

        double x = 6367558.4968 * B - sin(2 * B) * (16002.89 + 66.9607 * pow(sin(B), 2) + 0.3515 * pow(sin(B), 4)
            - pow(l, 2) * (1594561.25 + 5336.535 * pow(sin(B), 2) + 26.790 * pow(sin(B), 4) + 0.149 * pow(sin(B), 6)
            + pow(l, 2) * (672483.4 - 811219.9 * pow(sin(B), 2) + 5420.0 * pow(sin(B), 4) - 10.6 * pow(sin(B), 6)
            + pow(l, 2) * (278194 - 830174 * pow(sin(B), 2) + 572434 * pow(sin(B), 4) - 16010 * pow(sin(B), 6)
            + pow(l, 2) * (109500 - 574700 * pow(sin(B), 2) + 863700 * pow(sin(B), 4) - 398600 * pow(sin(B), 6)
        )))));

        double y = (5 + 10 * n) * 1e5 + l * cos(B) * (6378245 + 21346.1415 * pow(sin(B), 2) + 107.1590 * pow(sin(B), 4)
            + 0.5977 * pow(sin(B), 6) + pow(l, 2) * (1070204.16 - 2136826.66 * pow(sin(B), 2) + 17.98 * pow(sin(B), 4) - 11.99 * pow(sin(B), 6)
            + pow(l, 2) * (270806 - 1523417 * pow(sin(B), 2) + 1327645
                * pow(sin(B), 4) - 21701 * pow(sin(B), 6)
            + pow(l, 2) * (79690 - 866190 * pow(sin(B), 2) + 1730360 * pow(sin(B), 4) - 945460 * pow(sin(B), 6)
        ))));

        return new Point2D.Double(x, y);
    }

    private static Point2D xy2ll(Point2D source) {

        double x = source.getX();
        double y = source.getY();

        int n = (int) floor(y * 1e-6);
        double beta = x / 6367558.4968;

        double B0 = beta + sin(2 * beta) * (0.00252588685 - 0.00001491860 * pow(sin(beta), 2) + 0.00000011904 * pow(sin(beta), 4));

        double z0 = (y - (10 * n + 5) * 1e5) / (6378245 * cos(B0));

        double deltaB = -pow(z0, 2) * sin(2 * B0) * (0.251684631 - 0.003369263 * pow(sin(B0), 2) + 0.000011276 * pow(sin(B0), 4)
            - pow(z0, 2) * (0.10500614 - 0.04559916 * pow(sin(B0), 2) + 0.00228901 * pow(sin(B0), 4)
            - 0.00002987 * pow(sin(B0), 6) - pow(z0, 2) * (0.042858 - 0.025318 * pow(sin(B0), 2) + 0.014346 * pow(sin(B0), 4)
            - 0.001264 * pow(sin(B0), 6) - pow(z0, 2) * (0.01672 - 0.00630 * pow(sin(B0), 2) + 0.01188 * pow(sin(B0), 4)
            - 0.00328 * pow(sin(B0), 6)
        ))));

        double l = z0 * (1 - 0.0033467108 * pow(sin(B0), 2) - 0.0000056002 * pow(sin(B0), 4) - 0.0000000187 * pow(sin(B0), 6)
            - pow(z0, 2) * (0.16778975 + 0.16273586 * pow(sin(B0), 2) - 0.00052490 * pow(sin(B0), 4) - 0.00000846 * pow(sin(B0), 6)
            - pow(z0, 2) * (0.0420025 + 0.1487407 * pow(sin(B0), 2) + 0.0059420 * pow(sin(B0), 4) - 0.0000150 * pow(sin(B0), 6)
            - pow(z0, 2) * (0.01225 + 0.09477 * pow(sin(B0), 2) + 0.03282 * pow(sin(B0), 4) - 0.00034 * pow(sin(B0), 6)
            - pow(z0, 2) * (0.0038 + 0.0524 * pow(sin(B0), 2) + 0.0482 * pow(sin(B0), 4) + 0.0032 * pow(sin(B0), 6)
        )))));

        double B = B0 + deltaB;
        double L = 6 * (n - 0.5) / 57.29577951 + l;

        return new Point2D.Double(B, L);
    }
}

