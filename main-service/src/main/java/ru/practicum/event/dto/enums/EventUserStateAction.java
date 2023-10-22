package ru.practicum.event.dto.enums;

public enum EventUserStateAction {

    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static boolean isValid(String value) {
        for (EventUserStateAction stateAction : values()) {
            if (stateAction.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
