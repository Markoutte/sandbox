package me.markoutte.sandbox.algorithms.sort;

import java.util.Arrays;

public class MergeSorting implements Sorting {

    private static final int sentinel = Integer.MAX_VALUE;

    @Override
    public void sort(int[] input) {
        sort(input, 0, input.length - 1);
    }

    private void sort(int[] input, int p, int r) {
        if (p < r) {
            int q = (p + r) / 2;
            sort(input, p, q);
            sort(input, q + 1, r);
            merge(input, p, q, r);
        }
    }

    private void merge(int[] input, int p, int q, int r) {
        // we have q - p + 1 card for left (plus one for sentinel)
        // so p + (q - p + 1) + 1 = q + 2
        int[] left = Arrays.copyOfRange(input, p, q + 2);
        // ... and have r - q card for right (plus one for sentinel)
        // so (q + 1) + (r - q) + 1 = r + 2
        int[] right = Arrays.copyOfRange(input, q + 1, r + 2);
        left[left.length - 1] = sentinel;
        right[right.length - 1] = sentinel;
//        debug(input, p, q, r, left, right);
        for (int k = p, i = 0, j = 0; k < r + 1; k++) {
            if (left[i] < right[j]) {
                input[k] = left[i++];
            } else {
                input[k] = right[j++];
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void debug(int[] input, int p, int q, int r, int[] left, int[] right) {
        String sentinel = String.valueOf(MergeSorting.sentinel);
        System.out.println(Arrays.toString(input).replace(sentinel, "∞"));
        System.out.println("\t" + String.format("p=%d, q=%d, r=%d", p, q, r));
        System.out.println("\t" + Arrays.toString(left).replace(sentinel, "∞") + Arrays.toString(right).replace(sentinel, "∞"));
    }
}
