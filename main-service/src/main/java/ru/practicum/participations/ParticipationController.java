package ru.practicum.participations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participations.dto.ParticipationRequestDto;
import ru.practicum.participations.service.ParticipationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ParticipationController {
    private final ParticipationService service;

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getUserParticipationRequestList(
            @PathVariable long userId) {
        return null;
    }

    @PostMapping("/users/{userId}/requests") //условия в спецификации
    public ParticipationRequestDto createParticipationRequest(
            @PathVariable long userId,
            @RequestParam long eventId) {
        return null;
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel") //условия в спецификации
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable long userId,
            @RequestParam long requestId) {
        return null;
    }


}
