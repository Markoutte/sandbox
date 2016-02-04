package me.markoutte.sandbox.stupid.staticInitizializationDeadlock;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-02-02
 */
public class SubClass extends SuperClass {

    public SubClass() {
        System.out.println(getClass().getCanonicalName() + " init ");
    }
}
