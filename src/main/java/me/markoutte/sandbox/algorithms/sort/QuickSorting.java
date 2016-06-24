package me.markoutte.sandbox.algorithms.sort;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-06-24
 */
public class QuickSorting implements Sorting {
    @Override
    public void sort(int[] input) {
        sort(input, 0, input.length - 1);
    }

    private void sort(int[] input, int p, int r) {
        if (p < r) {
            int q = partition(input, p, r);
            sort(input, p, q - 1);
            sort(input, q + 1, r);
        }
    }

    private int partition(int[] input, int p, int r) {
        int i = p - 1;
        for (int j = p; j < r; j++) {
            if (input[j] <= input[r]) {
                exchange(input, ++i, j);
            }
        }
        exchange(input, i + 1, r);
        return i + 1;
    }
}
