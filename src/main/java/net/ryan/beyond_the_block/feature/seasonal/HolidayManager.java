package net.ryan.beyond_the_block.feature.seasonal;


import net.minecraft.server.MinecraftServer;

import java.time.LocalDate;

public class HolidayManager {

    static MinecraftServer currentServer;
    public static void setServer(MinecraftServer server) {
        currentServer = server;
        updateHoliday();
    }

    public enum Holiday {
        NONE, HALLOWEEN, CHRISTMAS, APRIL_FOOLS, VALENTINES;

        public boolean isActive() {
            return this != NONE && this == getCurrentHoliday();
        }

        public static Holiday getCurrentHoliday() {
            LocalDate now = LocalDate.now();
            int day = now.getDayOfMonth();
            return switch (now.getMonth()) {
                case OCTOBER -> day == 31 ? HALLOWEEN : NONE;
                case DECEMBER -> CHRISTMAS;
                case FEBRUARY -> day == 14 ? VALENTINES : NONE;
                case APRIL -> day == 1 ? APRIL_FOOLS : NONE;
                default -> NONE;
            };
        }
    }
    private static Holiday currentHoliday = Holiday.NONE;
    // Call this to update the holiday (e.g., at server start or daily tick)
    public static void updateHoliday() {
        Holiday newHoliday = Holiday.getCurrentHoliday();

        // Run end-hooks for any holidays that are not active today
        for (Holiday holiday : Holiday.values()) {
            if (holiday != Holiday.NONE && holiday != newHoliday) {
                HolidayFeatureRegistry.deactivateHoliday(holiday);
            }
        }

        if (newHoliday != currentHoliday) {
            // Activate new holiday
            HolidayFeatureRegistry.activateHoliday(newHoliday);
        }
        currentHoliday = newHoliday;
    }

}
