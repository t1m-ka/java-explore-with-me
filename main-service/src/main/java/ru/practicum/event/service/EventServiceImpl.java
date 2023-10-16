package ru.practicum.event.service;

import org.springframework.stereotype.Service;
import ru.practicum.event.dto.*;
import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public List<EventShortDto> getUserEventList(long userId, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto getUserEventFullByEventId(long userId, long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEventByUser(long userId, long eventId, UpdateEventRequest updateEventDto) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getUserEventParticipationRequests(long userId, long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult changeParticipationStatus(long userId, Long eventId,
            EventRequestStatusUpdateRequest statusUpdateRequest) {
        return null;
    }

    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<String> states, List<Long> categories,
            String rangeStart, String rangeEnd, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto updateEventByAdmin(long eventId, UpdateEventRequest updateEventDto) {
        return null;
    }

    @Override
    public List<EventShortDto> getEventFiltered(String text, List<Long> categories, boolean paid, String rangeStart,
            String rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        return null;
    }

    @Override
    public EventFullDto getEventById(long eventId) {
        return null;
    }
}
