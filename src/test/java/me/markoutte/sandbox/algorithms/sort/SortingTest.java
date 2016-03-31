package me.markoutte.sandbox.algorithms.sort;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-31
 */
public class SortingTest {

    private int[] input;
    private int[] output;

    @Before
    public void setUp() {
        input = simple();
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

    private int[] simple() {
        return new int[]{5, 2, 8, 4, 9, 6, 1, 3, 7, 0};
    }

    private int[] random() {
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

    private static int[] worst() {
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