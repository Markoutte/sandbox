package me.markoutte.sandbox.algorithms.sort;

import java.util.Arrays;

public class MergeSorting implements Sorting {

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
        int[] left = Arrays.copyOfRange(input, p, q + 2);
        int[] right = Arrays.copyOfRange(input, q + 1, r + 2);
        left[left.length - 1] = Integer.MAX_VALUE;
        right[right.length - 1] = Integer.MAX_VALUE;

//        System.out.println(Arrays.toString(input).replace("2147483647", "∞"));
//        System.out.println("\t" + String.format("p=%d, q=%d, r=%d", p, q, r));
//        System.out.println("\t" + Arrays.toString(left).replace("2147483647", "∞") + "" + Arrays.toString(right).replace("2147483647", "∞"));

        for (int k = p, i = 0, j = 0; k <= r; k++) {
            if (left[i] < right[j]) {
                input[k] = left[i++];
            } else {
                input[k] = right[j++];
            }
        }
    }
}
