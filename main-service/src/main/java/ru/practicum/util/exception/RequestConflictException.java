package ru.practicum.util.exception;

public class RequestConflictException extends CustomException {
    public RequestConflictException(String reason, String message) {
        super(reason, message);
    }
}
