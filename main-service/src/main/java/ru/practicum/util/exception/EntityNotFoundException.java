package ru.practicum.util.exception;

public class EntityNotFoundException extends CustomException {
    public EntityNotFoundException(String message, String reason) {
        super(message, reason);
    }
}
