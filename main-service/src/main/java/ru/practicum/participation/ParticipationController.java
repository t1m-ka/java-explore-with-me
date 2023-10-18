package ru.practicum.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.participation.service.ParticipationService;

import java.util.List;

import static ru.practicum.util.VariableValidator.validateNotNullObject;
import static ru.practicum.util.VariableValidator.validatePositiveNumber;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ParticipationController {
    private final ParticipationService service;

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getUserParticipationRequestList(
            @PathVariable Long userId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        return service.getUserParticipationRequestList(userId);
    }

    @PostMapping("/users/{userId}/requests")
    public ParticipationRequestDto createParticipationRequest(
            @PathVariable Long userId,
            @RequestParam(required = false) Long eventId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        return service.createParticipationRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable Long userId,
            @RequestParam Long requestId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(requestId, "requestId");
        validatePositiveNumber(requestId, "requestId");
        return service.cancelParticipationRequest(userId, requestId);
    }


}
