package me.markoutte.sandbox.algorithms.sort;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-31
 */
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
