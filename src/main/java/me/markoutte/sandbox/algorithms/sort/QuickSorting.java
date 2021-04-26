package me.markoutte.sandbox.algorithms.sort;

public class QuickSorting implements Sorting {
    @Override
    public void sort(int[] input) {
        sort(input, 0, input.length - 1);
    }

    protected void sort(int[] input, int p, int r) {
        if (p < r) {
            int q = partition(input, p, r);
            sort(input, p, q - 1);
            sort(input, q + 1, r);
        }
    }

    protected int partition(int[] input, int p, int r) {
        int i = p - 1;
        for (int j = p; j < r; j++) {
            if (input[j] <= input[r]) {
                exchange(input, ++i, j);
            }
        }
        exchange(input, i + 1, r);
        return i + 1;
    }

    public static class Random extends QuickSorting {

        private static final java.util.Random rand = new java.util.Random();

        @Override
        protected void sort(int[] input, int p, int r) {
            if (p < r) {
                int q = randomizePartition(input, p, r);
                sort(input, p, q - 1);
                sort(input, q + 1, r);
            }
        }

        private int randomizePartition(int[] input, int p, int r) {
            int i = rand.nextInt((r - p) + 1) + p;
            exchange(input, i, r);
            return partition(input, p, r);
        }
    }
}
