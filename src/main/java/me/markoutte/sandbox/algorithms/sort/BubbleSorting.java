package me.markoutte.sandbox.algorithms.sort;

public class BubbleSorting implements Sorting {
    @Override
    public void sort(int[] input) {
        for (int i = 0; i < input.length; i++) {
            boolean swapped = false;
            for (int j = input.length - 1; j > i; j--) {
                if (input[j] < input[j - 1]) {
                    exchange(input, j, j-1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
}
