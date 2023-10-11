package ru.practicum.util.exception;

public class ValidationException extends CustomException {
    public ValidationException(String message, String reason) {
        super(message, reason);
    }
}
