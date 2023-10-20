package ru.practicum.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.participation.service.ParticipationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ParticipationController {
    private final ParticipationService service;

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getUserParticipationRequestList(
            @PathVariable Long userId) {
        return service.getUserParticipationRequestList(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(
            @PathVariable Long userId,
            @RequestParam(required = false) Long eventId) {
        return service.createParticipationRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        return service.cancelParticipationRequest(userId, requestId);
    }


}
