package me.markoutte.sandbox.algorithms.sort;

public class SelectionSorting implements Sorting {

    @Override
    public void sort(int[] input) {
        for (int j = 0; j < input.length - 1; j++) {
            int index = j;
            for (int i = j; i < input.length; i++) {
                if (input[i] < input[index]) {
                    index = i;
                }
            }
            exchange(input, j, index);
        }
    }
}
