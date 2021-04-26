package me.markoutte.sandbox.algorithms.sort;

public interface Sorting {

    void sort(int[] input);

    default void exchange(int[] input, int i, int j) {
        if (input[i] != input[j]) {
            input[i] = input[i] ^ input[j];
            input[j] = input[i] ^ input[j];
            input[i] = input[i] ^ input[j];
        }
    }

}
