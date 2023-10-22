package ru.practicum.event.dto;

import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;

import java.util.List;

import static ru.practicum.category.dto.CategoryMapper.toCategoryDto;
import static ru.practicum.user.dto.UserMapper.toUserShortDto;

public class EventMapper {
    public static EventFullDto toEventFullDto(Event event, List<CommentFullDto> comments) {
        return new EventFullDto(
                event.getAnnotation(),
                toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                toUserShortDto(event.getInitiator()),
                new Location(event.getLat(), event.getLon()),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews(),
                comments
        );
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                toUserShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                event.getViews()
        );
    }
}
