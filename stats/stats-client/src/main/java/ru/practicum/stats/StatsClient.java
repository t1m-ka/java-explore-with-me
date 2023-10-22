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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    public StatsClient(@Value("${stats-service.url}") String serverUrl,
            RestTemplateBuilder builder,
            ObjectMapper objectMapper) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build(),
                objectMapper
        );
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters;
        ResponseEntity<Object> responseEntity;
        String formattedStart = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedEnd = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (uris != null) {
            String urisString = String.join(",", uris);
            parameters = Map.of(
                    "start", formattedStart,
                    "end", formattedEnd,
                    "uris", urisString,
                    "unique", unique);
            responseEntity = get("?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        } else {
            parameters = Map.of(
                    "start", formattedStart,
                    "end", formattedEnd,
                    "unique", unique);
            responseEntity = get("?start={start}&end={end}&unique={unique}", parameters);
        }
        return objectMapper.convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    }
}
