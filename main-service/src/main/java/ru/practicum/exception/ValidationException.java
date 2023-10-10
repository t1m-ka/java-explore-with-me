package ru.practicum.exception;

public class ValidationException extends CustomException {
    public ValidationException(String message, String reason) {
        super(message, reason);
    }
}
