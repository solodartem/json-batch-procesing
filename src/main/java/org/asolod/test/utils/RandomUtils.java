package org.asolod.test.utils;

import java.util.Random;

public class RandomUtils {

    private static Random r;

    public static int generatedIntFromRange(int low, int high ) {
        r = new Random();
        return r.nextInt(high-low) + low;
    }
}
