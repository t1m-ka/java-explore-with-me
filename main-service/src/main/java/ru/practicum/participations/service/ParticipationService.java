package ru.practicum.participations.service;

import ru.practicum.participations.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    List<ParticipationRequestDto> getUserParticipationRequestList(long userId);

    ParticipationRequestDto createParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);
}
