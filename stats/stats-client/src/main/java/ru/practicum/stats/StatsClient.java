package ru.practicum.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.exception.ServerErrorHandler.handleExceptionFromServer;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    private final ObjectMapper objectMapper;

    public StatsClient(@Value("${stats-service.url}") String serverUrl,
            RestTemplateBuilder builder,
            ObjectMapper objectMapper) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.objectMapper = objectMapper;
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters;
        ResponseEntity<Object> responseEntity;
        if (uris != null) {
            String urisString = String.join(",", uris);
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "uris", urisString,
                    "unique", unique);
            responseEntity = get("?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        } else {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "unique", unique);
            responseEntity = get("?start={start}&end={end}&unique={unique}", parameters);
        }
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            handleExceptionFromServer(responseEntity, objectMapper);
        return objectMapper.convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    }
}
