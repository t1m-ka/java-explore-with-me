package ru.practicum.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exception.ValidationException;

import static ru.practicum.hit.HitValidator.validateHitDto;

@RestController
@RequestMapping(path = "/hit")
@RequiredArgsConstructor
public class HitController {
    private final HitClient client;

    @PostMapping
    public void saveHit(@RequestBody HitDto hitDto) {
        if (validateHitDto(hitDto)) {
            client.saveHit(hitDto);
        } else {
            throw new ValidationException("The received entity is not correct");
        }
    }
}
