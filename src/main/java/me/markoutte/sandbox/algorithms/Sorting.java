package me.markoutte.sandbox.algorithms;

import java.util.Arrays;

// todo write initialization, maintain and termination for each sorting algorithms
public final class Sorting {

    public static void main(String[] args) {
        int[] input = {5, 2, 4, 6, 1, 3};
        int[] output = selectionSort(input);

        print(output);
    }

    private static int[] insertionSort(int[] input) {
        for (int j = 1; j < input.length; j++) {
            int value = input[j];
            for (int i = j - 1; i >= 0 && value < input[i];) {
                input[i + 1] = input[i];
                i--;
                input[i + 1] = value;
            }

        }
        return input;
    }

    private static int[] selectionSort(int[] input) {
        for (int j = 0; j < input.length - 1; j++) {
            int index = j;
            for (int i = j; i < input.length; i++) {
                if (input[index] > input[i]) {
                    index = i;
                }
            }
            int temp = input[j];
            input[j] = input[index];
            input[index] = temp;
        }
        return input;
    }

    @SuppressWarnings("unused")
    private static int[] worstInput() {
        int[] worst = new int[10_000];
        for (int i = 0; i < worst.length; i++) {
            worst[i] = worst.length - i;
        }
        return worst;
    }

    private static void print(int[] output) {
        System.out.println(Arrays.toString(output));
    }

}
