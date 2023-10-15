package ru.practicum.util.exception;

public class OperationForbiddenException extends CustomException {
    public OperationForbiddenException(String reason, String message) {
        super(reason, message);
    }
}
