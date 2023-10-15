package ru.practicum.events.dto.enums;

public enum EventAdminStateAction {
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static boolean isValid(String value) {
        for (EventAdminStateAction stateAction : values()) {
            if (stateAction.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
