package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.participation.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> getUserEventList(Long userId, int from, int size);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getUserEventFullByEventId(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest updateEventDto);

    List<ParticipationRequestDto> getUserEventParticipationRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeParticipationStatus(Long userId, Long eventId,
            EventRequestStatusUpdateRequest statusUpdateRequest);

    List<EventFullDto> searchEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            int from,
            int size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updateEventDto);

    List<EventShortDto> getEventFiltered(
            HttpServletRequest request,
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            boolean onlyAvailable,
            String sort,
            int from,
            int size);

    EventFullDto getEventById(HttpServletRequest request, Long eventId);
}
