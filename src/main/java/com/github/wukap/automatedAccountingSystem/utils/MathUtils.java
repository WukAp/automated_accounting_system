package com.github.wukap.automatedAccountingSystem.utils;

public class MathUtils {
    private MathUtils() {
    }

    public static int findGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return findGCD(b, a % b);
    }

    public static int findGCD(int a, int b, int c) {
        return findGCD(findGCD(a, b), c);
    }
}
