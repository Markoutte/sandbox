package me.markoutte.sandbox.stupid.staticInitizializationDeadlock;

public class RunExample {

    public static void main(String[] args) {
        new Thread(SuperClass::new).start();
        new SubClass();
    }

}
