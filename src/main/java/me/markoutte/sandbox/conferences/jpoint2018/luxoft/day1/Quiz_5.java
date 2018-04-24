package me.markoutte.sandbox.conferences.jpoint2018.luxoft.day1;

import java.util.HashSet;

/**
 * 1) 0
 * 2) 1
 * 3) 100
 * 4) Runtime exception
 * 5) Compilation error
 */
public class Quiz_5 {

    public static void main(String[] args) {
        HashSet<Short> set = new HashSet<>();
        for (short i = 0; i < 100; i++) {
            set.add(i);
            set.remove(i - 1);
        }
        System.out.println(set.size());
    }
    
}
