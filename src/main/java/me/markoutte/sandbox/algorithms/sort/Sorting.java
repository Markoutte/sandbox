package me.markoutte.sandbox.algorithms.sort;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-31
 */
public interface Sorting {

    void sort(int[] input);

    default String getName() {
        return getClass().getSimpleName().replace("Sorting", "");
    }

}
