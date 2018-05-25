package me.markoutte.sandbox.conferences.jpoint2018.luxoft.day2;

/**
 * 1). true
 * 2). false
 * 3). Compilation error
 */
public class Quiz_3 {

    public static void main(String[] args) {
        System.out.println(
                true?false:true == true?false:true
        );
    }
    
}
