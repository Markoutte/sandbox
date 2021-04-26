package me.markoutte.sandbox.stupid.staticInitizializationDeadlock;

public class SubClass extends SuperClass {

    public SubClass() {
        System.out.println(getClass().getCanonicalName() + " init ");
    }
}
