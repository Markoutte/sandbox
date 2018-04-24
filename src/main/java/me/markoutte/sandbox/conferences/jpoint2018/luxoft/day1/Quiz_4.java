package me.markoutte.sandbox.conferences.jpoint2018.luxoft.day1;

import java.util.stream.Stream;

/**
 * 1) 1AB2AB
 * 2) 1A2A1B2B
 * 3) 12AB
 * 4) 12ABAB
 * 5) Runtime exception
 * 6) Compilation error
 */
public class Quiz_4 {

    public static void main(String[] args) {
        Stream<String> stream =
                Stream.of("A", "B");
        System.out.print(1);
        stream.peek(System.out::print);
        System.out.print(2);
        stream.forEach(System.out::print);
    }
    
}
