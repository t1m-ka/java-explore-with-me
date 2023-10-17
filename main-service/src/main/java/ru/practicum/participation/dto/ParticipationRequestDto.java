package ru.practicum.participation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.participation.model.ParticipationRequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ParticipationRequestDto {
    private LocalDateTime created;

    private long event;

    private long id;

    private long requester;

    private ParticipationRequestStatus requestStatus;

}
