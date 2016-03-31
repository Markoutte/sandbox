package me.markoutte.sandbox.algorithms.sort;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-31
 */
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
            int temp = input[j];
            input[j] = input[index];
            input[index] = temp;
        }
    }
}
