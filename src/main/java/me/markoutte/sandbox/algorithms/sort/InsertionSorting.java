package me.markoutte.sandbox.algorithms.sort;

public class InsertionSorting implements Sorting {

    @Override
    public void sort(int[] input) {
        for (int j = 1; j < input.length; j++) {
            int value = input[j];
            for (int i = j - 1; i >= 0 && value < input[i]; i--) {
                input[i + 1] = input[i];
                input[i] = value;
            }
        }
    }
}
