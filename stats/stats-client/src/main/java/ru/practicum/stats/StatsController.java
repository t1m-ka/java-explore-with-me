package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsClient client;

    @GetMapping
    public List<StatsDto> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        if (start.isAfter(end))
            throw new ValidationException("Incorrect period");
        return client.getStats(start, end, uris, unique);
    }
}
//
