package me.markoutte.sandbox.competitions.leetcode;



import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

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
        System.out.println(isMatch("aa", "a")); // false
        System.out.println(isMatch("aa", "a*")); // true
        System.out.println(isMatch("ab", ".*")); // true
        System.out.println(isMatch("aaba", "ab*a*c*a")); // false
        System.out.println(isMatch("asb", "asb")); // true
        System.out.println(isMatch("asb", "avb")); // false
        System.out.println(isMatch("asb", "a.b")); // true
        System.out.println(isMatch("abc", ".*c")); // true
        System.out.println(isMatch("abc", ".*")); // true
        System.out.println(isMatch("abfec", "a.*c")); // true
        System.out.println(isMatch("helloworldandme", "hel*o.*and.*")); // true
        System.out.println(isMatch("abaa", "a.a.")); // true
        System.out.println(isMatch("babbcabcaabbbacaca", "bb*.c*.c*b*b.*c")); // false
    }

    private static final char EPS = (char) 0;
    
    private static final char[] alphabet = IntStream.range('a', 'z' + 1)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString()
            .toCharArray();
    private static final char[] alphabetWithEps = new char[alphabet.length + 1]; static {
        alphabetWithEps[0] = EPS;
        System.arraycopy(alphabet, 0, alphabetWithEps, 1, alphabet.length);
    }
    
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
                states[i + 1] = new Q();
                states[i - 1].removeSuccessor(symbols[i - 1], states[i]);
                states[i] = null;
                states[i - 1].addSuccessor(symbols[i - 1], states[i - 1]);
                states[i - 1].addSuccessor(EPS, states[i + 1]);
            } else {
                states[i + 1] = new Q();
                states[i].addSuccessor(symbols[i], states[i + 1]);
            }
        }
        // update states indexes
        for (int i = 0; i < states.length; i++) {
            Q state = states[i];
            if (state != null) {
                state.setIndex(i);
            }
        }
        
//        System.out.println(states[0]);
//        // If current is DFA (without * and .) this check should work
//        Q<Q> qurrent = states[0];
//        for (char c : s.toCharArray()) {
//            //
//            qurrent = qurrent.successors(c).stream().findFirst().orElse(null);
//            if (qurrent == null) {
//                return false;
//            }
//        }
//        if (true) {
//            return qurrent == states[states.length - 1];
//        }

        // NFA -> DFA
        Queue<S> queue = new ArrayDeque<>();
        S s0 = new S(states, states[0]);
        s0.eps();
        queue.offer(s0);
        Map<Long, S> visited = new HashMap<>();
        
        while (!queue.isEmpty()) {
            final S current = queue.poll();
            visited.put(current.getId(), current);
            for (char symbol : current.symbols()) {
                final S aNew = new S(states);
                for (Q q : current.getStates()) {
                    aNew.addStates(q.successors(symbol));
                }
                aNew.eps();
                // magic: find the same node in visited to reuse it
                final S anOld = visited.getOrDefault(aNew.getId(), aNew);
                
                current.addSuccessor(symbol, anOld);
                if (!anOld.isEmpty() && !visited.containsKey(anOld.getId())) {
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

        return StreamSupport.stream(current.getStates().spliterator(), false).anyMatch(q -> q == states[states.length - 1]);
    }
    
    /**
     * State of a deterministic finite automation (DFA).
     */
    private abstract static class N<T extends N> {
        private static final AtomicLong g = new AtomicLong();
        @SuppressWarnings("unused")
        protected final long idForDebug = g.getAndIncrement();
        private final Map<Character, Set<T>> successors = new HashMap<>();

        public Set<T> successors(char symbol) {
            if (successors.containsKey('.') && symbol != EPS) {
                Set<T> result = new HashSet<>(successors.get('.'));
                result.addAll(successors.getOrDefault(symbol, Collections.emptySet()));
                return result;
            } else {
                return Collections.unmodifiableSet(successors.getOrDefault(symbol, Collections.emptySet()));
            }
        }

        public void addSuccessor(char symbol, T next) {
            successors.computeIfAbsent(symbol, character -> new HashSet<>()).add(next);
        }

        public void removeSuccessor(char symbol, T removed) {
            successors.computeIfAbsent(symbol, character -> new HashSet<>()).remove(removed);
        }

        public char[] symbols() {
            char[] symbols = new char[successors.size()];
            int i = 0;
            for (Character character : successors.keySet()) {
                symbols[i++] = character;
            }
            return symbols;
        }

        @Override
        public String toString() {
            return toString(0, new HashSet<>());
        }

        protected String toString(int indent, Set<N<T>> alreadyPrinted) {
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

        public abstract String nodeName();
    }

    private static final class Q extends N<Q> {

        private int index;
        
        public String nodeName() {
            return "q%d:".formatted(idForDebug);
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    } 
    
    /**
     * State of a nondeterministic finite automation (NFA).
     */
    private static final class S extends N<S> {

        private final BitSet states;
        private final BitSet cache;
        private final Q[] source;

        public S(Q[] source) {
            this(source, new Q[0]);
        }

        public S(Q[] source, Q... states) {
            this.source = source;
            this.states = new BitSet(source.length);
            this.cache = new BitSet(source.length);
            for (Q state : states) {
                add(state);
            }
        }

        public boolean isEmpty() {
            return this.states.isEmpty();
        }

        public void add(Q state) {
            this.states.set(state.index, true);
        }

        public void remove(Q state) {
            this.states.set(state.index, false);
        }

        public long getId() {
            long[] longs = this.states.toLongArray();
            if (longs.length != 1) {
                throw new IllegalStateException("By the task maximum 20 symbols are can be passed. It can be stored in one long, but " + longs.length + " is used");
            }
            return longs[0];
        }

        public Iterable<Q> getStates() {
            return () -> new Iterator<>() {
                Q next = null;
                int index = -1;
                final int size = source.length;
                
                @Override
                public boolean hasNext() {
                    tryLoad();
                    return next != null;
                }

                @Override
                public Q next() {
                    if (next == null) {
                        tryLoad();
                        if (next == null) {
                            throw new NoSuchElementException();
                        }
                    }
                    return next;
                }

                private void tryLoad() {
                    for (++index; index < size; index++) {
                        if (states.get(index)) {
                            next = source[index];
                            return;
                        }
                    }
                    next = null;
                }
            };
        }

        public void addStates(java.util.Collection<Q> states) {
            for (Q state : states) {
                add(state);
            }
        }
        
        public boolean containsAll(java.util.Collection<Q> states) {
            cache.clear();
            for (Q state : states) {
                cache.set(state.index);
            }
            return cache.equals(this.states);
        }

        @Override
        public char[] symbols() {
            Set<Character> symbols = new HashSet<>();
            for (Q state : getStates()) {
                for (char s : state.symbols()) {
                    if (s == '.') {
                        return alphabet;
                    }
                    symbols.add(s);
                }
            }
            char[] result = new char[symbols.size()];
            int i = 0;
            for (Character symbol : symbols) {
                result[i++] = symbol;
            }
            return result;
        }

        public void eps() {
            Queue<Q> e = new ArrayDeque<>();
            getStates().forEach(e::offer);
            while (!e.isEmpty()) {
                Set<Q> epsClosure = e.poll().successors(EPS);
                addStates(epsClosure);
                e.addAll(epsClosure);
            }
        }

        @Override
        public String nodeName() {
            return "s%d:".formatted(idForDebug);
        }
    }
}
