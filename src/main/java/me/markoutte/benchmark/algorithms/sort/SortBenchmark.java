package me.markoutte.benchmark.algorithms.sort;

import me.markoutte.sandbox.algorithms.sort.*;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@State(Scope.Benchmark)
public class SortBenchmark {

    @Benchmark
    public void insertionSort() {
        Sorting sorting = new InsertionSorting();
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = 1000 - i;
        }
        sorting.sort(array);
    }

    @Benchmark
    public void selectionSort() {
        Sorting sorting = new SelectionSorting();
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = 1000 - i;
        }
        sorting.sort(array);
    }

    @Benchmark
    public void bubbleSort() {
        Sorting sorting = new BubbleSorting();
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = 1000 - i;
        }
        sorting.sort(array);
    }

    @Benchmark
    public void mergeSort() {
        Sorting sorting = new MergeSorting();
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = 1000 - i;
        }
        sorting.sort(array);
    }

    @Benchmark
    public void heapSort() {
        Sorting sorting = new HeapSorting();
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = 1000 - i;
        }
        sorting.sort(array);
    }

}
