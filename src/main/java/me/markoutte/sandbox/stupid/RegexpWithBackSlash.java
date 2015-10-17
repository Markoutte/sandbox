package me.markoutte.sandbox.stupid;

public class RegexpWithBackSlash {

    public static void main(String[] args) {
        System.out.println(".".replaceAll(".", "/"));
        System.out.println(".".replaceAll(".", "\\")); // <-- throws IllegalArgumentException
    }

}
