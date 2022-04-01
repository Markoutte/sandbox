package me.markoutte.sandbox.competitions.leetcode;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
 *
 * You may assume that each input would have exactly one solution, and you may not use the same element twice.
 *
 * You can return the answer in any order.
 */
public class TwoSum {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(twoSumFast(new int[]{3, 2, 4}, 6)));
    }

    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (i != j && target == nums[i] + nums[j]) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException();
    }

    public static int[] twoSumFast(int[] nums, int target) {
        int[] copy = Arrays.copyOf(nums, nums.length);
        Arrays.sort(copy);
        for (int i = 0; i < copy.length; i++) {
            int j = Arrays.binarySearch(copy, target - copy[i]);
            if (j >= 0) {
                return new int[]{find(nums, copy[i], false), find(nums, copy[j], true)};
            }
        }
        throw new IllegalArgumentException();
    }

    private static int find(int[] nums, int target, boolean backward) {
        for (int i = 0; i < nums.length; i++) {
            int j = backward ? nums.length - i - 1 : i;
            if (target == nums[j]) {
                return j;
            }
        }
        return -1;
    }
    
}
