package me.markoutte.benchmarks.algorithms.sort;

import me.markoutte.sandbox.algorithms.sort.*;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@State(Scope.Benchmark)
public class SortBenchmark {

    private static final int[] origin = new int[10_000];
    static {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = origin.length - i;
        }
    }

    @Benchmark
    public void insertionSort() {
        Sorting sorting = new InsertionSorting();
        sorting.sort(Arrays.copyOf(origin, origin.length));
    }

    @Benchmark
    public void binaryInsertionSort() {
        Sorting sorting = new BinaryInsertionSorting();
        sorting.sort(Arrays.copyOf(origin, origin.length));
    }

    @Benchmark
    public void selectionSort() {
        Sorting sorting = new SelectionSorting();
        sorting.sort(Arrays.copyOf(origin, origin.length));
    }

    @Benchmark
    public void bubbleSort() {
        Sorting sorting = new BubbleSorting();
        sorting.sort(Arrays.copyOf(origin, origin.length));
    }

    @Benchmark
    public void mergeSort() {
        Sorting sorting = new MergeSorting();
        sorting.sort(Arrays.copyOf(origin, origin.length));
    }

    @Benchmark
    public void heapSort() {
        Sorting sorting = new HeapSorting();
        sorting.sort(Arrays.copyOf(origin, origin.length));
    }

    @Benchmark
    public void quickSort() {
        Sorting sorting = new QuickSorting();
        sorting.sort(Arrays.copyOf(origin, origin.length));
    }

    @Benchmark
    public void randomQuickSort() {
        Sorting sorting = new QuickSorting.Random();
        sorting.sort(Arrays.copyOf(origin, origin.length));
    }

}
