package ru.practicum.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
public class ApiError {
    private List<StackTraceElement> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

    public <T extends CustomException> ApiError(T ex, String status) {
        this.errors = Arrays.asList(ex.getStackTrace());
        this.message = ex.getMessage();
        this.reason = ex.getReason();
        this.status = status;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}