package me.markoutte.sandbox.competitions.leetcode;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
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
//        System.out.println(isMatch("aa", "a")); // false
//        System.out.println(isMatch("aa", "a*")); // true
//        System.out.println(isMatch("ab", ".*")); // true
        System.out.println(isMatch("aaba", "ab*a*c*a")); // true
//        System.out.println(isMatch("asb", "asb"));
//        System.out.println(isMatch("asb", "avb"));
//        System.out.println(isMatch("asb", "a.b"));
//        System.out.println(isMatch("abc", ".*c"));
//        System.out.println(isMatch("abc", ".*"));
//        System.out.println(isMatch("abfec", "a.*c"));
//        System.out.println(isMatch("helloworldandme", "hel*o.*and.*"));
    }

    private static char[] alphabet = IntStream.range('a', 'z' + 1)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString()
            .toCharArray();
    
    public static boolean isMatch(String s, String p) {
        char[] symbols = p.toCharArray();
        // We gonna keep the states of NFA in arrays to facilitate initial creation.
        // Real number of states can be less that total number of symbols,
        // because of special character '*', but it doesn't matter.
        // Note that only valid patter is expected (the p like "***" can cause an error).
        Q[] states = new Q[symbols.length + 1];
        states[0] = new Q(); // initial state
        for (int i = 0; i < symbols.length; i++) {
            // states[i] means 'previous'
            if (symbols[i] == '*') {
                // when * is met we need to get previous state and merge it with previous of this previous
                states[i - 1].addSuccessor(symbols[i - 1], states[i - 1]);
                states[i - 1].removeSuccessor(symbols[i - 1], states[i]);
                states[i] = null;
                states[i + 1] = states[i - 1];
            } else {
                states[i + 1] = new Q();
                states[i].addSuccessor(symbols[i], states[i + 1]);
            }
        }
        
        System.out.println(states[0]);
//        // If current is DFA (without * and .) this check should work
//        Q current = states[0];
//        for (char c : s.toCharArray()) {
//            //
//            current = current.successors(c).stream().findFirst().orElse(null);
//            if (current == null) {
//                return false;
//            }
//        }
//        return current == states[states.length - 1];

        // NFA -> DFA
        Queue<S> queue = new ArrayDeque<>();
        S s0 = new S(states[0]);
        queue.offer(s0);
        Set<S> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            final S current = queue.poll();
            visited.add(current);
            for (char symbol : alphabet) {
                final S aNew = new S();
                // add already seen
                for (Q q : current.getStates()) {
                    Set<Q<S>> successors = q.successors(symbol);
                    aNew.addStates(successors);
                }
                // magic: find the same node in visited to reuse it
                final S anOld = visited.stream().filter(n -> {
                    if (n.getStates().size() != aNew.getStates().size()) return false;
                    return n.containsAll(aNew.getStates());
                }).findFirst().orElse(aNew);
                current.addSuccessor(symbol, anOld);
                if (!anOld.getStates().isEmpty() && !visited.contains(anOld)) {
                    queue.offer(anOld);
                }
            }
        }

//        System.out.println(s0);
        S current = s0;
        for (char c : s.toCharArray()) {
            //
            Set<S> successors = current.successors(c);
            if (successors.size() > 1) {
                throw new IllegalStateException("Only one successor should be found for 1 symbol in DFA");
            }
            current = successors.stream().findFirst().orElse(null);
            if (current == null) {
                return false;
            }
        }

        return current.getStates().contains(states[states.length - 1]);
    }

    /**
     * State of a deterministic finite automation (DFA).
     */
    private static class Q<T extends Q> {
        private static final AtomicLong g = new AtomicLong();
        @SuppressWarnings("unused")
        protected final long idForDebug = g.getAndIncrement();
        private final Map<Character, Set<T>> successors = new HashMap<>();

        public Set<T> successors(char symbol) {
            return Collections.unmodifiableSet(successors.getOrDefault(symbol, Collections.emptySet()));
        }

        public void addSuccessor(char symbol, T next) {
            if (symbol == '.') {
                for (int i = 0; i < alphabet.length; i++) {
                    addSuccessor(alphabet[i], next);
                }
            } else {
                successors.computeIfAbsent(symbol, character -> new HashSet<>()).add(next);
            }
        }

        public void removeSuccessor(char symbol, Q removed) {
            if (symbol == '.') {
                for (int i = 0; i < alphabet.length; i++) {
                    removeSuccessor(alphabet[i], removed);
                }
            } else {
                successors.computeIfAbsent(symbol, character -> new HashSet<>()).remove(removed);
            }
        }

        @Override
        public String toString() {
            return toString(0, new HashSet<>());
        }

        protected String toString(int indent, Set<Q> alreadyPrinted) {
            alreadyPrinted.add(this);
            String indentation = "\t".repeat(indent);
            List<Character> symbols = new ArrayList<>(successors.keySet());
            StringBuilder builder = new StringBuilder(nodeName());
            symbols.stream().sorted().forEach(character -> {
                builder.append("\n" + indentation);
                builder.append(character);
                builder.append(" -> ");
                successors(character).forEach(state -> {
                    if (!alreadyPrinted.contains(state)) {
                        //noinspection unchecked
                        builder.append(state.toString(indent + 1, alreadyPrinted));
                    } else {
                        builder.append(nodeName());
                    }
                });
            });
            return builder.toString();
        }

        public String nodeName() {
            return "q%d:".formatted(idForDebug);
        }
    }

    /**
     * State of a nondeterministic finite automation (NFA).
     */
    private static final class S extends Q<S> {

        private final Set<Q<S>> states = new HashSet<>();

        public S() {
        }

        public S(Q<S>... states) {
            //noinspection UseBulkOperation
            Arrays.stream(states).forEach(this.states::add);
        }

        public void add(Q<S> state) {
            states.add(state);
        }

        public void remove(Q<S> state) {
            states.remove(state);
        }

        public java.util.Collection<Q<S>> getStates() {
            return Collections.unmodifiableCollection(states);
        }

        public void addStates(java.util.Collection<Q<S>> states) {
            this.states.addAll(states);
        }

        public boolean containsAll(java.util.Collection<Q<S>> states) {
            return this.states.containsAll(states);
        }

        @Override
        public String nodeName() {
            return "q%d:".formatted(idForDebug);
        }
    }
}
