package ru.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static ru.practicum.exception.ServerExceptionHandler.handleExceptionFromServer;

@Slf4j
public class BaseClient {
    protected final RestTemplate rest;

    protected final ObjectMapper objectMapper;

    public BaseClient(RestTemplate rest, ObjectMapper objectMapper) {
        this.rest = rest;
        this.objectMapper = objectMapper;
    }

    protected <T> ResponseEntity<T> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    protected <T> ResponseEntity<T> post(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
    }

    private <T> ResponseEntity<T> makeAndSendRequest(HttpMethod method, String path,
            @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<T> statsServiceResponse = null;
        try {
            if (parameters != null) {
                statsServiceResponse = rest.exchange(path, method, requestEntity, new ParameterizedTypeReference<>() {}, parameters);
            } else {
                statsServiceResponse = rest.exchange(path, method, requestEntity, new ParameterizedTypeReference<>() {});
            }
        } catch (HttpStatusCodeException e) {
            log.error("Request processing error");
            log.error("Status code = {}", e.getRawStatusCode());
            log.error("Headers = {}", e.getResponseHeaders());
            log.error("ResponseBody = {}", e.getResponseBodyAsString());
            handleExceptionFromServer(ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString()), objectMapper);
        }
        return prepareGatewayResponse(statsServiceResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static <T> ResponseEntity<T> prepareGatewayResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}
