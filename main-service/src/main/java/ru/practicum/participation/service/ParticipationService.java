package ru.practicum.participation.service;

import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    List<ParticipationRequestDto> getUserParticipationRequestList(long userId);

    ParticipationRequestDto createParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);
}
