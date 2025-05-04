package com.sonorous.util.methods;

import java.util.Random;

public class RandomUtils {

    private static Random random = new Random();

    public static double getRandomDouble(double lowerBound, double upperBound) {
        return random.nextDouble(lowerBound, upperBound);
    }

    public static boolean getBoolean(){
        return random.nextBoolean();
    }

    public static int getRandomInt(int max) {
        return random.nextInt(max);
    }
}
