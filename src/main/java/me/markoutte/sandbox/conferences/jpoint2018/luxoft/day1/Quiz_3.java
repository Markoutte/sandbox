package me.markoutte.sandbox.conferences.jpoint2018.luxoft.day1;

/**
 * 1) 123
 * 2) 12
 * 3) 23
 * 4) 13
 * 5) 1
 * 6) 2
 * 7) 3
 */
public class Quiz_3 {

    public static void main(String[] args) {

        System.out.println(new String("t" + "r" + "u" + "e") == new String("true"));
        
        String str = "true";
        if ("t" + "r" + "u" + "e" == "true")
            System.out.print("1");
        if (str + "" == "true")
            System.out.print("2");
        if (str.replace('T', 't') == "true")
            System.out.print("3");
    }
    
}
