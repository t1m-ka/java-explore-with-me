package ru.practicum.exception;

public abstract class CustomException extends RuntimeException {
    private final String reason;

    public CustomException(String reason, String message) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
