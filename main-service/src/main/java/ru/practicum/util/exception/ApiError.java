package ru.practicum.util.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timestamp;

    private List<StackTraceElement> errors;

    public <T extends CustomException> ApiError(T ex, String status) {
        this.status = status;
        this.reason = ex.getReason();
        this.message = ex.getMessage();
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.errors = Arrays.asList(ex.getStackTrace());
    }

    public  ApiError(DataIntegrityViolationException ex, String status) {
        this.status = status;
        this.reason = ex.getLocalizedMessage();
        this.message = ex.getMessage();
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.errors = Arrays.asList(ex.getStackTrace());
    }
}