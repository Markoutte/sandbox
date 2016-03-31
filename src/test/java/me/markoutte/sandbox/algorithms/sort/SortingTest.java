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
        Sorting sorting = new InsertionSorting();
        sorting.sort(output);
        print(sorting);
    }

    @Test
    public void selectionSort() {
        Sorting sorting = new SelectionSorting();
        sorting.sort(output);
        print(sorting);
    }

    @Test
    public void mergeSort() {
        MergeSorting sorting = new MergeSorting();
        sorting.sort(output);
        print(sorting);
    }

    @After
    public void tearDown() {
        for (int i = 0; i < input.length; i++) {
            Assert.assertEquals(i, output[i]);
        }
    }

    private int[] generate() {
        return new int[]{5, 2, 8, 4, 9, 6, 1, 3, 7, 0};
    }

    private void print(Sorting sorting) {
        System.out.println(String.format("%s: %s -> %s", sorting.getName(), Arrays.toString(input), Arrays.toString(output)));
    }

}