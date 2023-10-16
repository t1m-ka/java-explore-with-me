package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    List<EventShortDto> getUserEventList(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto newEventDto);

    EventFullDto getUserEventFullByEventId(long userId, long eventId);

    EventFullDto updateEventByUser(long userId, long eventId, UpdateEventRequest updateEventDto);

    List<ParticipationRequestDto> getUserEventParticipationRequests(long userId, long eventId);

    EventRequestStatusUpdateResult changeParticipationStatus(long userId, Long eventId,
            EventRequestStatusUpdateRequest statusUpdateRequest);

    List<EventFullDto> searchEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            int from,
            int size);

    EventFullDto updateEventByAdmin(long eventId, UpdateEventRequest updateEventDto);

    List<EventShortDto> getEventFiltered(
            String text,
            List<Long> categories,
            boolean paid,
            String rangeStart,
            String rangeEnd,
            boolean onlyAvailable,
            String sort,
            int from,
            int size);

    EventFullDto getEventById(long eventId);
}
