package net.ryan.beyond_the_block.utils;

import java.util.concurrent.ThreadLocalRandom;

public class TickTimeUtil {

    public static int secondsToTicks(double seconds) {
        return (int) Math.round(seconds * 20);
    }

    public static double ticksToSeconds(int ticks) {
        return ticks / 20.0;
    }

    public static int minutesToTicks(double minutes) {
        return (int) Math.round(minutes * 60 * 20); // 60 seconds per minute × 20 ticks per second
    }

    public static double ticksToMinutes(int ticks) {
        return ticks / (20.0 * 60); // ticks → seconds → minutes
    }

    public static int randomTicks(double minSeconds, double maxSeconds) {
        if (minSeconds > maxSeconds) {
            throw new IllegalArgumentException("minSeconds cannot be greater than maxSeconds");
        }
        int minTicks = secondsToTicks(minSeconds);
        int maxTicks = secondsToTicks(maxSeconds);
        return ThreadLocalRandom.current().nextInt(minTicks, maxTicks + 1);
    }

    /**
     * Returns a random number of ticks between minMinutes and maxMinutes (inclusive).
     */
    public static int randomTicksMinutes(double minMinutes, double maxMinutes) {
        if (minMinutes > maxMinutes) {
            throw new IllegalArgumentException("minMinutes cannot be greater than maxMinutes");
        }
        int minTicks = minutesToTicks(minMinutes);
        int maxTicks = minutesToTicks(maxMinutes);
        return ThreadLocalRandom.current().nextInt(minTicks, maxTicks + 1);
    }
}
