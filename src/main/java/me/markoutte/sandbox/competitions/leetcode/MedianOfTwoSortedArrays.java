package me.markoutte.sandbox.competitions.leetcode;

import java.util.Arrays;

/**
 * Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
 *
 * The overall run time complexity should be O(log (m+n)).
 */
public class MedianOfTwoSortedArrays {

    public static void main(String[] args) {
        System.out.println(findMedianSortedArrays(
                new int[]{1, 2},
                new int[]{3, 4}
        ));
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] merged = new int[nums1.length + nums2.length];
        int i = 0, j = 0;
        for (int k = 0; k < merged.length; k++) {
            if ((i < nums1.length ? nums1[i] : Integer.MAX_VALUE) < (j < nums2.length ? nums2[j] : Integer.MAX_VALUE)) {
                merged[k] = nums1[i++];
            } else {
                merged[k] = nums2[j++];
            }
        }

        if (merged.length % 2 == 1) {
            return merged[merged.length / 2];
        } else {
            return (merged[merged.length / 2 - 1] + merged[merged.length / 2]) / 2.0;
        }
    }
    
    public static double findMedianSortedArraysNaive(int[] nums1, int[] nums2) {
        int[] merged = new int[nums1.length + nums2.length];
        System.arraycopy(nums1, 0, merged, 0, nums1.length);
        System.arraycopy(nums2, 0, merged, nums1.length, nums2.length);
        Arrays.sort(merged);
        if (merged.length % 2 == 1) {
            return merged[merged.length / 2];
        } else {
            return (merged[merged.length / 2 - 1] + merged[merged.length / 2]) / 2.0;
        }
    }
    
}
