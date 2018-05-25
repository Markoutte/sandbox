package me.markoutte.sandbox.conferences.jpoint2018.luxoft.day2;

import java.util.Arrays;
import java.util.List;

/**
 * 1). integer
 * 2). double
 * 3). object
 * 4). Runtime exception
 * 5). Compilation exception
 */
public class Quiz_2 {
    
    public void func(Integer i) {
        System.out.println("integer"); }
    public void func(Double i) {
        System.out.println("double"); }
    public void func(Object i) {
        System.out.println("object"); }

    public static void main(String[] args) {
        List<?> nums = Arrays.asList(1/2);
        new Quiz_2().func(nums.get(0));
    }
}
