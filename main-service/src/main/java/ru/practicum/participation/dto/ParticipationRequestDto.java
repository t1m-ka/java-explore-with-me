package ru.practicum.participation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.participation.model.ParticipationRequestStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private long event;

    private long id;

    private long requester;

    private ParticipationRequestStatus status;

}
