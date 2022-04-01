package me.markoutte.sandbox.competitions.leetcode;

/**
 * Write an efficient algorithm that searches for a value target in an m x n integer matrix matrix. This matrix has the following properties:
 *
 * Integers in each row are sorted from left to right.
 * The first integer of each row is greater than the last integer of the previous row.
 */
public class SearchA2DMatrix {

    public static void main(String[] args) {
        System.out.println(searchMatrix(
                new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}},
                3
        ));
    }

    public static boolean searchMatrix(int[][] matrix, int target) {
        int rows = matrix.length;
        if (rows == 0) return false;
        int cols = matrix[0].length;
        if (cols == 0) return false;

        int low = 0;
        int hi = rows * cols - 1;

        while (low <= hi) {
            int mid = (low + hi) / 2;
            int val = matrix[mid / cols][mid % cols];
            if (val < target) {
                low = mid + 1;
            } else if (val > target) {
                hi = mid - 1;
            } else {
                return true;
            }
        }

        return false;
    }
    
}
