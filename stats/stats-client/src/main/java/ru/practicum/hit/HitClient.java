package ru.practicum.hit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

@Service
public class HitClient extends BaseClient {
    private static final String API_PREFIX = "/hit";

    @Autowired
    public HitClient(@Value("${stats-service.url}") String serverUrl,
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

    public void saveHit(HitDto hitDto) {
        ResponseEntity<HitDto> responseEntity = post("", null, hitDto);
    }
}
