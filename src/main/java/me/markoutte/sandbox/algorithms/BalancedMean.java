package me.markoutte.sandbox.algorithms;

public class BalancedMean extends Mean {

    private long start;
    private long prev;

    public void add(long x) {
        if (count == 0) {
            start = x;
        }
        prev = x;
        super.add(x - start);
    }

    public double getMean() {
        return super.getMean() + start;
    }

}
