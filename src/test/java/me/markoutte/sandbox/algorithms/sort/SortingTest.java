package me.markoutte.sandbox.algorithms.sort;

import org.junit.*;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-31
 */
public class SortingTest {

    private int[] input;
    private int[] output;
    private Sequence sequence;

    @Before
    public void setUp() {
        sequence = Sequence.SIMPLE;
        input = sequence.generate();
        output = Arrays.copyOf(input, input.length);
    }

    @Test
    public void insertionSort() {
        new InsertionSorting().sort(output);
    }

    @Test
    public void selectionSort() {
        new SelectionSorting().sort(output);
    }

    @Test
    public void mergeSort() {
        new MergeSorting().sort(output);
    }

    @After
    public void tearDown() {
        List<Integer> expected = new ArrayList<>(input.length);
        for (int i : input) expected.add(i);
        Collections.sort(expected);

        for (int i = 0; i < input.length; i++) {
            Assert.assertEquals(expected.get(i).intValue(), output[i]);
        }
    }

    public enum Sequence {
        SIMPLE {
            @Override
            public int[] generate() {
                return new int[]{5, 2, 8, 4, 9, 6, 1, 3, 7, 0};
            }
        },
        RANDOM {
            @Override
            public int[] generate() {
                List<Integer> numbers = new ArrayList<>();
                for (int i = 0; i < 80; i++) {
                    numbers.add(i);
                }
                Collections.shuffle(numbers);
                int[] input = new int[numbers.size()];
                for (int i = 0; i < input.length; i++) {
                    input[i] = numbers.get(i);
                }
                return input;
            }
        },
        WORST {
            @Override
            public int[] generate() {
                int[] worst = new int[10_000];
                for (int i = 0; i < worst.length; i++) {
                    worst[i] = worst.length - i - 1;
                }
                return worst;
            }
        }
        ;

        public abstract int[] generate();
    }

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            System.out.println(String.format("%s (%d μs): %s -> %s",
                    description.getMethodName().replace("Sort", ""),
                    TimeUnit.NANOSECONDS.toMicros(nanos),
                    Arrays.toString(input), Arrays.toString(output)
                    ));
        }
    };

}