package ru.practicum.participation.service;

import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    List<ParticipationRequestDto> getUserParticipationRequestList(Long userId);

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
