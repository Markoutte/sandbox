package me.markoutte.sandbox.algorithms.sort;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-31
 */
public class SortingTest {

    private int[] input;
    private int[] output;

    @Before
    public void setUp() {
        input = generate();
        output = Arrays.copyOf(input, input.length);
    }

    @Test
    public void insertionSort() {
        Sorting sort = new InsertionSorting();
        sort.sort(output);
        print(sort);
    }

    @Test
    public void selectionSort() {
        Sorting sort = new SelectionSorting();
        sort.sort(output);
        print(sort);
    }

    @After
    public void tearDown() {
        int value = output[0];
        for (int i = 0; i < input.length; i++) {
            int current = output[i];
            Assert.assertTrue(String.format("value %d is greater than %d", value, current), value <= current);
            value = current;
        }
    }

    private int[] generate() {
        return new int[]{5, 2, 4, 6, 1, 3};
//        return worstInput();
    }

    private static int[] worstInput() {
        int[] worst = new int[10_000];
        for (int i = 0; i < worst.length; i++) {
            worst[i] = worst.length - i;
        }
        return worst;
    }

    private void print(Sorting sorting) {
        System.out.println(String.format("%s: %s -> %s", sorting.getName(), Arrays.toString(input), Arrays.toString(output)));
    }

}