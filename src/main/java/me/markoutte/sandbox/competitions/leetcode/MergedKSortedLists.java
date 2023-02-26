package me.markoutte.sandbox.competitions.leetcode;

import java.util.*;

public class MergedKSortedLists {

    public static ListNode usePriorityQueue(ListNode[] lists) {
        class IntBinaryHeap {
            private final ListNode[] heap;
            private int size = 0;
            IntBinaryHeap(int size) {
                this.heap = new ListNode[size];
            }

            public void add(ListNode a) {
                heap[size++] = a;
                int i = size - 1;
                do {
                    i = (i - 1) / 2;
                    heapify(i);
                } while (i != 0);
            }

            public ListNode pollAndAdd() {
                if (size == 0) {
                    throw new IllegalArgumentException();
                }
                ListNode result = heap[0];
                if (result.next != null) {
                    heap[0] = result.next;
                    heapify(0);
                } else {
                    heap[0] = heap[size - 1];
                    size--;
                    if (size > 0) {
                        heapify(0);
                    }
                }
                return result;
            }

            public boolean isNotEmpty() {
                return size > 0;
            }

            private void heapify(int i) {
                int left = i * 2 + 1;
                int right = i * 2 + 2;
                int min = i;
                if (left < size && heap[left].val < heap[min].val) {
                    min = left;
                }
                if (right < size && heap[right].val < heap[min].val) {
                    min = right;
                }
                if (min != i) {
                    swap(i, min);
                    heapify(min);
                }
            }

            private void swap(int i, int j) {
                ListNode tmp = heap[i];
                heap[i] = heap[j];
                heap[j] = tmp;
            }
        }

        IntBinaryHeap heap = new IntBinaryHeap(lists.length);

        for (ListNode list : lists) {
            if (list != null) {
                heap.add(list);
            }
        }

        ListNode fs = null;
        ListNode ln = null;
        while (heap.isNotEmpty()) {
            ListNode node = heap.pollAndAdd();
            if (ln == null) {
                ln = fs = node;
            } else {
                ln.next = node;
                ln = ln.next;
            }
        }
        return fs;
    }

    public static ListNode byLinearSearch(ListNode[] lists) {
        class Helper {
            private final ListNode[] currentNodes;

            Helper(ListNode[] currentNodes) {
                this.currentNodes = currentNodes;
            }
            public ListNode findNext() {
                ListNode current = null;
                int index = -1;
                for (int i = 0; i < currentNodes.length; i++) {
                    ListNode node = currentNodes[i];
                    if (current == null && node != null) {
                        current = node;
                        index = i;
                    } else if (current != null && node != null && current.val > node.val) {
                        current = node;
                        index = i;
                    }
                }
                if (index >= 0) {
                    Objects.requireNonNull(current);
                    currentNodes[index] = current.next;
                }
                return current;
            }
        }

        var helper = new Helper(lists);
        ListNode node;
        ListNode fs = null;
        ListNode ln = null;
        while ((node = helper.findNext()) != null) {
            if (ln == null) {
                ln = fs = node;
            } else {
                ln.next = node;
                ln = ln.next;
            }
        }
        return fs;
    }

    public static ListNode justSortThis(ListNode[] lists) {
        int size = 0;
        for (ListNode list : lists) {
            if (list != null) {
                ListNode next = list;
                while (next != null) {
                    size++;
                    next = next.next;
                }
            }
        }
        int[] res = new int[size];
        int cur = 0;
        ListNode prev = null;
        for (ListNode list : lists) {
            if (prev != null) {
                prev.next = list;
            }
            if (list != null) {
                ListNode next = list;
                while (next != null) {
                    res[cur++] = next.val;
                    if (next.next == null) {
                        prev = next;
                    }
                    next = next.next;
                }
            }
        }
        if (res.length == 0) {
            return null;
        }
        Arrays.sort(res);
        ListNode ln = null;
        for (ListNode list : lists) {
            if (list != null) {
                ln = list;
                break;
            }
        }
        ListNode fs = ln;
        Objects.requireNonNull(ln);
        for (int re : res) {
            ln.val = re;
            ln = ln.next;
        }
        return fs;
    }

    public static void main(String[] args) {
//        ListNode[] nodes = {
//                new ListNode(1, new ListNode(4, new ListNode(5))),
//                new ListNode(1, new ListNode(3, new ListNode(4))),
//                new ListNode(2, new ListNode(6)),
//        };
//        ListNode[] nodes = new ListNode[1000];
//        for (int i = 0; i < nodes.length; i++) {
//            ListNode ln = null;
//            for (int j = 0; j < 60000; j++) {
//                if (ln == null) {
//                    ln = new ListNode(j);
//                    nodes[i] = ln;
//                } else {
//                    ln.next = new ListNode(j);
//                    ln = ln.next;
//                }
//            }
//        }
        ListNode[] nodes = {
                new ListNode(-10, new ListNode(-9, new ListNode(-9, new ListNode(-3, new ListNode(-1, new ListNode(-1, new ListNode(0))))))),
                new ListNode(-5),
                new ListNode(4),
                new ListNode(-8),
                null,
                new ListNode(-9, new ListNode(-6, new ListNode(-5, new ListNode(-4, new ListNode(-2, new ListNode(2, new ListNode(3))))))),
                new ListNode(-3, new ListNode(-3, new ListNode(-2, new ListNode(-1, new ListNode(0))))),
        };
//        ListNode[] nodes = {
//                null,
//                new ListNode(1),
//        };
//        
        ListNode n = byLinearSearch(nodes);
        while (n != null) {
            System.out.println(n.val);
            n = n.next;
        }
    }
    
}

class ListNode {
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

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
 
