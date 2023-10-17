package ru.practicum.event.model;

import ru.practicum.event.dto.enums.EventUserStateAction;
import ru.practicum.util.exception.OperationForbiddenException;
import ru.practicum.util.exception.ValidationException;

public enum EventState {
    PENDING,
    PUBLISHED,
    REJECTED,
    CANCELED;

    public static EventState fromString(String value) {
        for (EventState state : EventState.values()) {
            if (state.name().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new OperationForbiddenException("For the requested operation the conditions are not met",
                "Field: state. Error: значение статуса может быть: PENDING, PUBLISHED, REJECTED, CANCELED. "
                        + "Value:" + value);
    }

    public static boolean isValid(String value) {
        for (EventState state : values()) {
            if (state.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
