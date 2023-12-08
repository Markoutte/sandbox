package me.markoutte.sandbox.algorithms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

public class MeanTest {

    @Test
    public void test() {
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
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

}
