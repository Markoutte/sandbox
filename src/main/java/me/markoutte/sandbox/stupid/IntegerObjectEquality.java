package me.markoutte.sandbox.stupid;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-02
 */
public class IntegerObjectEquality {

    public static void main(String[] args) {
        Integer a = 100;
        Integer b = 100;
        System.out.println(a == b);

        Integer c = 300;
        Integer d = 300;
        System.out.println(c == d);
    }

}
