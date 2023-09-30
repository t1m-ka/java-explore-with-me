package ru.practicum.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

public class ServerExceptionHandler {
    public static void handleExceptionFromServer(ResponseEntity<String> responseEntity, ObjectMapper objectMapper) {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseEntity.getBody());
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
