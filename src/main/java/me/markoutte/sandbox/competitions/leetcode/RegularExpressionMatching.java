package me.markoutte.sandbox.competitions.leetcode;


import java.util.*;
import java.util.stream.IntStream;

/**
 * Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:
 * <p>
 * '.' Matches any single character.
 * '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 * <p>
 * Example 1:
 *
 * <pre>
 * Input: s = "aa", p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa".
 * </pre>
 * 
 * Example 2:
 *
 * <pre>
 * Input: s = "aa", p = "a*"
 * Output: true
 * Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
 * </pre>
 * 
 * Example 3:
 * 
 * <pre>
 * Input: s = "ab", p = ".*"
 * Output: true
 * Explanation: ".*" means "zero or more (*) of any character (.)".
 * </pre>
 *
 * <ol>Constraints:
 *
 * <li>1 <= s.length <= 20
 * <li>1 <= p.length <= 20
 * <li>s contains only lowercase English letters.
 * <li>p contains only lowercase English letters, '.', and '*'.
 * <li>It is guaranteed for each appearance of the character '*', there will be a previous valid character to match.
 * </ol>
 */
public class RegularExpressionMatching {

    public static void main(String[] args) {
//        System.out.println(isMatch("asb", "asb"));
//        System.out.println(isMatch("asb", "avb"));
//        System.out.println(isMatch("asb", "a.b"));
        System.out.println(isMatch("abc", ".*c"));
    }

    public static boolean isMatch(String s, String p) {
        char[] alphabet = new char['z' - 'a' + 1];
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = (char) ('a' + i);
        }
        char[] sChars = s.toCharArray();
        char[] pChars = p.toCharArray();
        State prev = null;
        State start = null;
        State finite = null;
        int exitPoll = pChars.length - 1;
        for (int i = 0; i < pChars.length; i++) {
            State state = new State(i);
            if (prev != null) {
                if (pChars[i - 1] == '*') {
                    continue;
                }
                if (pChars[i - 1] == '.') {
                    for (char c : alphabet) {
                        prev.next.computeIfAbsent(c, HashSet::new).add(state);
                    }
                } else {
                    prev.next.computeIfAbsent(pChars[i - 1], HashSet::new).add(state);
                }
                if (pChars[i] == '*') {
                    exitPoll--;
                    if (pChars[i - 1] == '.') {
                        for (char c : alphabet) {
                            prev.next.computeIfAbsent(c, HashSet::new).add(prev);
                        }
                    } else {
                        prev.next.computeIfAbsent(pChars[i - 1], HashSet::new).add(prev);
                    }
                }
            }
            if (i == 0) {
                start = state;
            }
            if (i == exitPoll) {
                finite = state;
            }
            prev = state;
        }

        Queue<Node> queue = new ArrayDeque<>();
        Node startStates = new Node();
        startStates.add(start);
        queue.offer(startStates);

        Set<Node> alreadySeen = new HashSet<>();
        alreadySeen.add(startStates);
        
        // (current state, char) => {next state, null}
        while (!queue.isEmpty()) {
            final Node currentNode = queue.poll();
            alreadySeen.add(currentNode);
            // currentStates + e-closure
            for (char a : alphabet) {
                Node newNode = new Node();
                for (State currentState : currentNode) {
                    Set<State> state = currentState.next.get(a);
                    if (state != null) {
                        newNode.addAll(state);
                    }
                }
                // magic??? if already has that node then replace
                // newStates = tryReplace(newStates, someCache)
                var mmm = alreadySeen.stream().filter(n -> {
                    if (n.size() != newNode.size()) return false;
                    return n.containsAll(newNode);
                }).findFirst().orElse(newNode); //todo only 0 or 1 must be found
                currentNode.next.put(a, mmm);
                if (!mmm.isEmpty() && !alreadySeen.contains(mmm)) {
                    queue.offer(mmm);
                }
            }
        }
        
        Node current = startStates;
        for (char c : sChars) {
            //
            current = current.next.get(c);
            if (current == null) {
                return false;
            }
        }

        return current.contains(finite);
    }
    
//    public static boolean isMatch(String s, String p) {
//        char[] alphabet = new char[]{'a', 'b', 'c', 'd', 'e', 's', 'v', 'x', 'y', 'z'};
//        State[] states = new State[p.length() + 1];
//        char[] sChars = s.toCharArray();
//        char[] pChars = p.toCharArray();
//        for (int i = 0; i <= pChars.length; i++) {
//            states[i] = new State(i);
//            if (i > 0) {
//                if (pChars[i - 1] == '.') {
//                    for (char c : alphabet) {
//                        states[i - 1].next.put(c, states[i]);
//                    }
//                } else {
//                    states[i - 1].next.put(pChars[i - 1], states[i]);
//                }
//            }
//        }
//        State current = states[0];
//        State finite = states[p.length()];
//
//        for (char c : sChars) {
//            current = current.next.get(c);
//            if (current == null) {
//                return false;
//            }
//        }
//        
//        return Objects.equals(current, finite);
//    }

    private static class State {
        final int id;
        Map<Character, Set<State>> next = new HashMap<>();

        State(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            State state = (State) o;

            return id == state.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
    
    private static class Node extends HashSet<State> {
        final Map<Character, Node> next = new HashMap<>();
    }
}
