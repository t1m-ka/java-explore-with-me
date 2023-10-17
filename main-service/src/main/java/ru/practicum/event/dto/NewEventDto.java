package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.event.model.Location;

@Getter
@AllArgsConstructor
public class NewEventDto {
    private String annotation;

    private Long categoryId;

    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    private String title;
}