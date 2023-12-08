package me.markoutte.sandbox.algorithms;

public class Mean {

    private int count = 0;
    private double mean = 0.0;

    public void add(double x) {
        count++;
        double delta = x - mean;
        mean += delta / count;
    }

    public double getMean() {
        return mean;
    }

}
