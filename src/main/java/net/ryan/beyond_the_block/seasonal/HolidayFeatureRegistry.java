package net.ryan.beyond_the_block.seasonal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class HolidayFeatureRegistry {

    private static final Map<HolidayManager.Holiday, List<Runnable>> START_FEATURES = new EnumMap<>(HolidayManager.Holiday.class);
    private static final Map<HolidayManager.Holiday, List<Runnable>> END_FEATURES = new EnumMap<>(HolidayManager.Holiday.class);

    // Register a start task
    public static void registerStart(HolidayManager.Holiday holiday, Runnable task) {
        START_FEATURES.computeIfAbsent(holiday, h -> new ArrayList<>()).add(task);
    }

    // Register an end task
    public static void registerEnd(HolidayManager.Holiday holiday, Runnable task) {
        END_FEATURES.computeIfAbsent(holiday, h -> new ArrayList<>()).add(task);
    }

    // Activate a holiday
    public static void activateHoliday(HolidayManager.Holiday holiday) {
        START_FEATURES.getOrDefault(holiday, List.of()).forEach(Runnable::run);
    }

    // Deactivate a holiday
    public static void deactivateHoliday(HolidayManager.Holiday holiday) {
        END_FEATURES.getOrDefault(holiday, List.of()).forEach(Runnable::run);
    }
}
