package ru.practicum.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.ValidationException;
import ru.practicum.hit.dto.HitDto;
import ru.practicum.hit.service.HitService;

import static ru.practicum.hit.dto.HitValidator.validateHitDto;

@RestController
@RequestMapping(path = "/hit")
@RequiredArgsConstructor
@Slf4j
public class HitController {
    private final HitService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody HitDto hitDto) {
        log.info("Поступил POST запрос на сохранение статистических данных (saveHit)");
        if (validateHitDto(hitDto))
            service.saveHit(hitDto);
        else
            throw new ValidationException("Поступивший объект некорректен");
    }
}
