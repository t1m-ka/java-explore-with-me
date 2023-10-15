package ru.practicum.events.dto.enums;

public enum EventRequestStatus {
    CONFIRMED,
    REJECTED;

    public static boolean isValid(String value) {
        for (EventRequestStatus requestStatus : values()) {
            if (requestStatus.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
