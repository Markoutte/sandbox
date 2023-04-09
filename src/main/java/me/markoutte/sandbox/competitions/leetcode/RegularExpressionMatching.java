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

    @SuppressWarnings("SpellCheckingInspection")
    public static void main(String[] args) {
        check("aa", "a", false);
        check("aa", "a*", true);
        check("ab", ".*", true);
        check("aaba", "ab*a*c*a", false);
        check("asb", "asb", true);
        check("asb", "avb", false);
        check("asb", "a.b", true);
        check("abc", ".*c", true);
        check("abc", ".*", true);
        check("abfec", "a.*c", true);
        check("helloworldandme", "hel*o.*and.*", true);
        check("abaa", "a.a.", true);
        check("babbcabcaabbbacaca", "bb*.c*.c*b*b.*c", false);
        check("baabbbaccbccacacc", "c*..b*a*a.*a..*c", true);
        check("mississippi", "mis*is*p*.", false);
        check("ippi", "p*.", false);
    }

    private static void check(String s, String p, boolean expected) {
        boolean match = isMatch(s, p);
        StringBuilder sb = new StringBuilder();
        if (match == expected) {
            sb.append("OK ");
        } else {
            sb.append("BAD");
        }
        sb.append(" ");
        sb.append(s);
        sb.append(", ");
        sb.append(p);
        sb.append(" => ");
        sb.append(match);
        System.out.println(sb);
    }

    private static final char EPS = (char) 0;
    private static final char ANY = '.';
    
    private static final char[] alphabet = IntStream.range('a', 'z' + 1)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString()
            .toCharArray();
    
    public static boolean isMatch(String s, String p) {
        char[] symbols = p.toCharArray();
        // We're going to keep the states of NFA in arrays to facilitate initial creation.
        // The real number of states can be less that total number of symbols,
        // because of special character '*', but it doesn't matter.
        // Note that only valid patter is expected (the p like "***" can cause an error).
        Q[] states = new Q[symbols.length + 1];
        states[0] = new Q(); // initial state
        for (int i = 0; i < symbols.length; i++) {
            // states[i] means 'previous'
            if (symbols[i] == '*') {
                // when * is met, we remove previous state `i`, 
                // but link state `i - 1` like this:
                // 1. the previous symbol will lead to the state `i - 1`
                // 2. epsilon moves to the new state.
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

        // NFA -> DFA
        Queue<S> queue = new ArrayDeque<>();
        S s0 = new S(states, states[0], eps(states[0]));
        queue.offer(s0);
        Map<Long, S> visited = new HashMap<>();
        
        while (!queue.isEmpty()) {
            final S current = queue.poll();
            for (char symbol : alphabet) {
                Iterable<Q> newStates = move(current, symbol);
                var newState = new S(states, join(newStates, eps(newStates)));
                if (!newState.isEmpty()) {
                    if (visited.containsKey(newState.getId())) {
                        newState = visited.getOrDefault(newState.getId(), newState);
                    } else {
                        queue.offer(newState);
                        visited.put(newState.getId(), newState);
                    }
                    current.addSuccessor(symbol, newState);
                }
            }
        }
        
        S current = s0;
        for (char c : s.toCharArray()) {
            //
            Iterator<S> successors = current.successors(c).iterator();
            if (!successors.hasNext()) {
                return false;
            }
            current = successors.next();
            if (successors.hasNext()) {
                throw new IllegalStateException("Only one successor should be found for 1 symbol in DFA");
            }
        }

        return StreamSupport.stream(current.getStates().spliterator(), false).anyMatch(q -> q == states[states.length - 1]);
    }

    private static Iterable<Q> move(S states, char symbol) {
        return move(states.getStates(), symbol);
    }

    private static Iterable<Q> move(Iterable<Q> states, char symbol) {
        List<Iterable<Q>> result = new LinkedList<>();
        for (Q q : states) {
            result.add(q.successors(symbol));
        }
        return () -> new JoinIterator<>(result);
    }

    public static Iterable<Q> eps(Q state) {
        return eps(Collections.singletonList(state));
    }

    public static Iterable<Q> eps(Iterable<Q> states) {
        List<Iterable<Q>> result = new ArrayList<>(100);
        for (Q state : states) {
            Iterable<Q> successors = state.successors(EPS);
            result.add(successors);
            result.add(eps(successors));
        }
        return () -> new JoinIterator<>(result);
    }

    @SafeVarargs
    private static <T> Iterable<T> join(Iterable<T>... iterables) {
        return () -> new JoinIterator<>(iterables);
    }
    
    /**
     * State of a deterministic finite automation (DFA).
     */
    private abstract static class N<T extends N<T>> {
        private static final AtomicLong g = new AtomicLong();
        @SuppressWarnings("unused")
        protected final long idForDebug = g.getAndIncrement();
        private final Map<Character, List<T>> successors = new HashMap<>();

        public Iterable<T> successors(char symbol) {
            if (symbol == ANY) {
                return () -> new JoinIterator<>(successors);
            }
            else if (successors.containsKey(ANY) && symbol != EPS) {
                return () -> new JoinIterator<>(successors.get(ANY), successors.getOrDefault(symbol, Collections.emptyList()));
            } else {
                return successors.getOrDefault(symbol, Collections.emptyList());
            }
        }

        public void addSuccessor(char symbol, T next) {
            successors.computeIfAbsent(symbol, character -> new LinkedList<>()).add(next);
        }

        public void removeSuccessor(char symbol, T removed) {
            successors.computeIfAbsent(symbol, character -> new LinkedList<>()).remove(removed);
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
                builder.append("\n").append(indentation);
                builder.append(character);
                builder.append(" -> ");
                successors(character).forEach(state -> {
                    if (!alreadyPrinted.contains(state)) {
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
        private final Q[] source;

        public S(Q[] source, Q state, Iterable<Q> others) {
            this(source, join(Collections.singletonList(state), others));
        }

        public S(Q[] source, Iterable<Q> states) {
            this.source = source;
            this.states = new BitSet(source.length);
            if (states != null) {
                for (Q state : states) {
                    add(state);
                }
            }
        }

        public boolean isEmpty() {
            return this.states.isEmpty();
        }

        public void add(Q state) {
            this.states.set(state.getIndex(), true);
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
                    if (next == null) {
                        tryLoad();
                    }
                    return next != null;
                }

                @Override
                public Q next() {
                    if (next == null) {
                        tryLoad();
                        if (next == null) {
                            throw new java.util.NoSuchElementException();
                        }
                    }
                    Q result = next;
                    next = null;
                    return result;
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

        @Override
        public String nodeName() {
            return "s%d:".formatted(idForDebug);
        }
    }
    
    private static class JoinIterator<E> implements Iterator<E> {

        private final Iterator<E>[] iterators;

        public JoinIterator(Map<Character, ? extends Iterable<E>> map) {
            //noinspection unchecked
            this.iterators = new Iterator[map.containsKey(EPS) ? map.size() - 1 : map.size()];
            int i = 0;
            for (Map.Entry<Character, ? extends Iterable<E>> entry : map.entrySet()) {
                if (entry.getKey() != EPS) {
                    this.iterators[i++] = entry.getValue().iterator();
                }
            }
        }
        
        public JoinIterator(java.util.Collection<? extends Iterable<E>> collection) {
            //noinspection unchecked
            this.iterators = new Iterator[collection.size()];
            int i = 0;
            for (Iterable<E> iterable : collection) {
                this.iterators[i++] = iterable.iterator();
            }
        }
        
        @SafeVarargs
        public JoinIterator(Iterable<E>... iterables) {
            //noinspection unchecked
            this.iterators = new Iterator[iterables.length];
            for (int i = 0; i < iterables.length; i++) {
                this.iterators[i] = iterables[i].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            for (Iterator<E> iterator : iterators) {
                if (iterator.hasNext()) return true;
            }
            return false;
        }

        @Override
        public E next() {
            for (Iterator<E> iterator : iterators) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
            throw new java.util.NoSuchElementException();
        }
    }

    @SuppressWarnings("unused") // for debugging
    private static <T> List<T> toList(Iterable<T> iterable) {
        var list = new ArrayList<T>();
        iterable.forEach(list::add);
        return list;
    } 
}
