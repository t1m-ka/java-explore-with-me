package ru.practicum.util.exception;

public class RequiredArgsMissingException extends CustomException {

    public RequiredArgsMissingException(String reason, String message) {
        super(reason, message);
    }
}
