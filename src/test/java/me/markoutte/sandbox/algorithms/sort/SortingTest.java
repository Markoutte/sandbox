package me.markoutte.sandbox.algorithms.sort;

import org.junit.*;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-31
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SortingTest {

    private static final int[] input = Sequence.RANDOM.generate();
    private static final int[] expected = Arrays.copyOf(input, input.length);
    private int[] output;

    @BeforeClass
    public static void initialize() {
        Arrays.sort(expected);
    }

    @Test
    public void insertionSort() {
        check(new InsertionSorting());
    }

    @Test
    public void selectionSort() {
        check(new SelectionSorting());
    }

    @Test
    public void mergeSort() {
        check(new MergeSorting());
    }

    @Test
    public void bubbleSort() {
        check(new BubbleSorting());
    }

    @Test
    public void heapSort() {
        check(new HeapSorting());
    }

    @Test
    public void quickSort() {
        check(new QuickSorting());
    }

    @Test
    public void randomQuickSort() {
        check(new QuickSorting.Random());
    }

    public void check(Sorting sorting) {
        output = Arrays.copyOf(input, input.length);
        sorting.sort(output);
        for (int i = 0; i < input.length; i++) {
            Assert.assertEquals(expected[i], output[i]);
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
                for (int i = 0; i < 10000; i++) {
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
            System.out.printf("%s (%d μs): %s -> %s%n",
                    description.getMethodName().replace("Sort", ""),
                    TimeUnit.NANOSECONDS.toMicros(nanos),
                    arrayToString(input), arrayToString(output));
        }

        private String arrayToString(int[] array) {
            return input.length > 1_000 ? "[...]" : Arrays.toString(array);
        }
    };

}