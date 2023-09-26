package ru.practicum.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.hit.dto.HitDto;
import ru.practicum.hit.service.HitService;

@RestController
@RequestMapping(path = "/hit")
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitService service;

    @PostMapping
    public void saveHit(@RequestBody HitDto hitDto) {
        log.info("Поступил POST запрос на сохранение статистических данных (saveHit)");
        service.saveHit(hitDto);
    }
}
