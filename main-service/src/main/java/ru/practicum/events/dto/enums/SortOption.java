package ru.practicum.events.dto.enums;

public enum SortOption {
    EVENT_DATE,
    VIEWS;

    public static boolean isValid(String value) {
        for (SortOption options : values()) {
            if (options.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
