package ru.practicum.util.exception;

public class EntityAlreadyExistsException extends CustomException {
    public EntityAlreadyExistsException(String reason, String message) {
        super(reason, message);
    }
}
