package me.markoutte.sandbox.stupid.staticInitizializationDeadlock;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-02-02
 */
public class SuperClass {

    static {
        System.out.println(SuperClass.class.getCanonicalName() + " static {");
        new SubClass();
        System.out.println("} static " + SuperClass.class.getCanonicalName());
    }

}
