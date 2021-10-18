package me.markoutte.sandbox.algorithms;

import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Math.*;

public class ShuntingYard {

    public static void main(String[] args) {
//        var input = "sin ( 2 + 3 ^ 4 ^ 5 )";
        var input = "2 + sin ( 5 + 3 ) * 8 + ( 9 * 7 ) !";
        var stack = new ArrayDeque<String>();
        var output = new ArrayList<String>();
        var digits = Pattern.compile("\\d+");
        var operators = Map.of(
                "^", -100,
                "*", 50,
                "/", 50,
                "+", 10,
                "-", 10);
        String[] tokens = input.split(" ");
        System.out.println("Tokens are " + Arrays.toString(tokens));
        for (String s : tokens) {
            if (digits.matcher(s).matches()) {
                output.add(s);
            }
            else if (s.equals(")")) {
                var top = stack.removeLast();
                while (!top.equals("(")) {
                    output.add(top);
                    top = stack.removeLast();
                }
            } else if (operators.containsKey(s)) {
                String top = stack.peekLast();
                if (top != null) {
                    Integer l = operators.get(top);
                    Integer r = operators.get(s);
                    if (!top.equals("(") && (l == null || abs(l) > abs(r) || (abs(l) == abs(r) && l >= 0))) {
                        output.add(stack.removeLast());
                    }
                }
                stack.addLast(s);
            }
            else {
                stack.addLast(s);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.removeLast());
        }

        output.forEach(s -> System.out.print(s + " "));
    }

}
