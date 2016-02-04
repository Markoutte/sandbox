package me.markoutte.sandbox.competitions;

import java.util.*;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2015-11-18
 */
class A {
    public static void main(String[] a) {
//        Scanner s = new Scanner(System.in);
//        int c = s.nextInt(), t = 0, v;
//        while (c-- > 0) t+=(v = s.nextInt()) > 0 ?v:0 ;
//        System.out.print(t);

        Scanner s = new Scanner(System.in);
        int t = 0;
        for (int c = s.nextInt(); c-- > 0;) t += Math.max(s.nextInt(), 0);
        System.out.println(t);
    }
}