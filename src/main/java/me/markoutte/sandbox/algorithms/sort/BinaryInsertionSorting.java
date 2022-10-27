package me.markoutte.sandbox.algorithms.sort;

import java.util.Arrays;

public class BinaryInsertionSorting implements Sorting {
    @Override
    public void sort(int[] input) {
        for (int i = 1; i < input.length; i++) {
            int value = input[i];
            int index = Arrays.binarySearch(input, 0, i, value);
            if (index < 0) {
                index = -(index + 1);
            }
            System.arraycopy(input, index, input, index + 1, i - index);
            input[index] = value;
        }
    }
}
