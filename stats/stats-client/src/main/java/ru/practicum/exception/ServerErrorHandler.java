package ru.practicum.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

public class ServerErrorHandler {
    public static void handleExceptionFromServer(ResponseEntity<Object> responseEntity, ObjectMapper objectMapper) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree((String) responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new UnexpectedServerResponseException("Request processing error");
        }
        String errorText = jsonNode.get("error").asText();

        switch (responseEntity.getStatusCode().value()) {
            case 400:
                throw new IllegalArgumentException(errorText);
            default:
                throw new UnexpectedServerResponseException("Request processing error");
        }
    }
}
