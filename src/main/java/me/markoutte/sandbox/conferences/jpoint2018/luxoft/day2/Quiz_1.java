package me.markoutte.sandbox.conferences.jpoint2018.luxoft.day2;

/**
 * 1) AB
 * 2) B
 * 3) NullPointerException
 * 3) Compilation error
 */
public class Quiz_1 {

    public static void print() {
        System.out.print("A");
    }
    
    public static void main(String[] args) {
        ((Quiz_1) null).print();
        System.out.print("B");
    }
    
}
