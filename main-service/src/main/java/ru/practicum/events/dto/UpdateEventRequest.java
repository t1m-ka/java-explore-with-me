package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.events.model.Location;

@Getter
@AllArgsConstructor
public class UpdateEventRequest {
    String annotation;

    Long categoryId;

    String description;

    String eventDate;

    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    String stateAction;

    String title;
}
