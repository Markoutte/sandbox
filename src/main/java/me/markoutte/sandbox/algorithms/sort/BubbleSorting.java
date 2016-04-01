package me.markoutte.sandbox.algorithms.sort;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-04-01
 */
public class BubbleSorting implements Sorting {
    @Override
    public void sort(int[] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = input.length - 1; j > i; j--) {
                if (input[j] < input[j - 1]) {
                    int temp = input[j];
                    input[j] = input[j - 1];
                    input[j - 1] = temp;
                }
            }
        }
    }
}
