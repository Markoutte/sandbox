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
        while (true) {
            int l = 2 * i + 1;
            int r = 2 * i + 2;
            int largest = i;
            if (l < heapsize && input[l] > input[largest])
                largest = l;
            if (r < heapsize && input[r] > input[largest])
                largest = r;
            if (largest != i) {
                exchange(input, i, largest);
                i = largest;
            } else {
                break;
            }
        }
    }
}
