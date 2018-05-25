package me.markoutte.sandbox.conferences.jpoint2018.luxoft.day2;

/**
 * 1). -2
 * 2). 0
 * 3). 2
 * 4). 4294967296 (2^32)
 * 5). Runtime exception
 * 6). Compilation error
 */
public class Quiz_5 {

    public static void main(String[] args) {
        int n = Integer.MAX_VALUE;
        n++;
        System.out.println(n + n);
    }
    
}
