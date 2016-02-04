package me.markoutte.sandbox.stupid.staticInitizializationDeadlock;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-02-02
 */
public class RunExample {

    public static void main(String[] args) {
        new Thread(SuperClass::new).start();
        new SubClass();
    }

}
