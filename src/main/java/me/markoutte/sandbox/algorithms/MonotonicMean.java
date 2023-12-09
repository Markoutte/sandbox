package me.markoutte.sandbox.algorithms;

public class MonotonicMean extends Mean {

    private final long start;
    private long prev;

    public MonotonicMean(long start) {
        this.start = start;
        this.prev = start;
    }

    public void add(long x) {
        if (x < prev) {
            throw new IllegalArgumentException("Is not monotonic value!");
        }
        prev = x;
        super.add(x - start);
    }

    public double getMean() {
        return super.getMean() + start;
    }

}
