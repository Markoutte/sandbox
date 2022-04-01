package me.markoutte.sandbox.competitions.leetcode;

/**
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
 *
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 */
public class AddTwoNumbers {

    public static void main(String[] args) {
        ListNode result = addTwoNumbers(
                createList(9999999),
                createList(9999)
        );
        int number = 0;
        while (result != null) {
            number *= 10;
            number += result.val;
            result = result.next;
        }
        System.out.println(number);
    }

    private static ListNode createList(int value) {
        if (value == 0) return new ListNode(0);
        ListNode head = null;
        ListNode result = null;
        while (value != 0) {
            ListNode node = new ListNode(value % 10);
            value = value / 10;
            if (result != null) {
                result.next = node;
            } else {
                head = node;
            }
            result = node;
        }
        return head;
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int inc = 0;
        ListNode head = null;
        ListNode result = null;
        while (true) {
            int cur = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + inc;
            ListNode node = new ListNode(cur % 10);
            inc = cur / 10;
            if (result != null) {
                result.next = node;
            } else {
                head = node;
            }
            result = node;
            l1 = l1 == null ? null : l1.next;
            l2 = l2 == null ? null : l2.next;
            if (l1 == null && l2 == null) {
                break;
            }
        }
        if (inc != 0) {
            result.next = new ListNode(inc);
        }
        return head;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
