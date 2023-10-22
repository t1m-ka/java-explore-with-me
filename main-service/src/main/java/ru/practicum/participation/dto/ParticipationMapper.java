package ru.practicum.participation.dto;

import ru.practicum.participation.model.ParticipationRequest;

public class ParticipationMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getCreated(),
                participationRequest.getEvent().getId(),
                participationRequest.getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getRequestStatus());
    }
}
