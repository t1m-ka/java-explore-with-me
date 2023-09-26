package ru.practicum.exception;

public class UnexpectedServerResponseException extends RuntimeException {
    public UnexpectedServerResponseException(String message) {
        super(message);
    }
}
