package me.markoutte.sandbox.algorithms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class MeanTest {

    @Test
    public void test() {
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            Mean mean = new Mean();
            double sum = 0.0;
            int runs = random.nextInt(1, 300);
            for (int j = 0; j < runs; j++) {
                double value = random.nextDouble();
                sum += value;
                mean.add(value);
            }

            Assertions.assertEquals(mean.getMean(), sum / runs, 1e-10);
        }
    }

    @Test
    public void test2() {
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            BalancedMean mean = new BalancedMean();
            long sum = 0;
            int runs = random.nextInt(1, 300);
            for (int j = 0; j < runs; j++) {
                long value = System.nanoTime();
                sum = StrictMath.addExact(sum, value);
                mean.add(value);
            }

            Assertions.assertEquals((double) sum / runs, mean.getMean(), 1E-8);
        }
    }

}
