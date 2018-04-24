package me.markoutte.sandbox.conferences.jpoint2018.luxoft.day1;

import java.util.Arrays;

/**
 * 1) [0, 2]
 * 2) [1, 2]
 * 3) Runtime exception
 * 4) Compilation error
 */
public class Quiz_2 {

    public static void main(String[] args) {
        String[] strings = {"1", "1"};
        Object[] objects = strings;
        objects[0] = 0;
        System.out.println(
                Arrays.toString(objects)
        );
    }
    
}
