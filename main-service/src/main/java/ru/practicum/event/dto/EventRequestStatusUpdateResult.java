package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;
import ru.practicum.participation.dto.ParticipationRequestStatus;

import java.util.List;

@Setter
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<Long> requestIds;

    private ParticipationRequestStatus status;
}
