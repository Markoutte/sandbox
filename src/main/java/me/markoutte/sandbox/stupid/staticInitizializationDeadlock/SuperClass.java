package me.markoutte.sandbox.stupid.staticInitizializationDeadlock;

public class SuperClass {

    static {
        System.out.println(SuperClass.class.getCanonicalName() + " static {");
        new SubClass();
        System.out.println("} static " + SuperClass.class.getCanonicalName());
    }

}
