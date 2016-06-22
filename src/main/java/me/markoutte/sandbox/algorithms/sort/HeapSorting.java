package me.markoutte.sandbox.algorithms.sort;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-06-22
 */
public class HeapSorting implements Sorting {

    @Override
    public void sort(int[] input) {
        buildHeap(input);
        for (int i = input.length - 1; i >= 1; i--) {
            exchange(input, 0, i);
            heapify(input, i, 0);
        }
    }

    private void buildHeap(int[] input) {
        for (int i = (input.length - 1) / 2; i >= 0; i--) {
            heapify(input, input.length, i);
        }
    }

    private void heapify(int[] input, int heapsize, int i) {
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        int largest = (l < heapsize && input[l] > input[i]) ? l : i;
        largest = (r < heapsize && input[r] > input[largest]) ? r : largest;
        if (largest != i) {
            exchange(input, i, largest);
            heapify(input, heapsize, largest);
        }
    }

    private void exchange(int[] input, int i, int largest) {
        input[i] = input[i] ^ input[largest];
        input[largest] = input[i] ^ input[largest];
        input[i] = input[i] ^ input[largest];
    }
}
