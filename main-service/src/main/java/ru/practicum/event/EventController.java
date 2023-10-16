package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

import static ru.practicum.event.dto.EventDtoValidator.*;
import static ru.practicum.util.VariableValidator.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService service;

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getUserEventList(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validatePageableParams(from, size);
        return service.getUserEventList(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto createEvent(
            @PathVariable Long userId,
            @RequestBody NewEventDto newEventDto) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNewEvent(newEventDto);
        return service.createEvent(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getUserEventFullByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        return service.getUserEventFullByEventId(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody UpdateEventRequest updateEventDto) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        validateUpdateEvent(updateEventDto, false);
        return service.updateEventByUser(userId, eventId, updateEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventParticipationRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        return service.getUserEventParticipationRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeParticipationStatus(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        validateNotNullObject(statusUpdateRequest, "EventRequestStatusUpdateRequest");
        validateUpdateRequestStatus(statusUpdateRequest);
        return service.changeParticipationStatus(userId, eventId, statusUpdateRequest);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        if (rangeStart != null)
            validateDateTimeFormat(rangeStart, "rangeStart");
        if (rangeEnd != null)
            validateDateTimeFormat(rangeEnd, "rangeEnd");
        validatePageableParams(from, size);
        return service.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(
            @PathVariable Long eventId,
            @RequestBody UpdateEventRequest updateEventDto) {
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        validateNotNullObject(updateEventDto, "UpdateEventAdminRequest");
        validateUpdateEvent(updateEventDto, true);
        return service.updateEventByAdmin(eventId, updateEventDto);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEventFiltered(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        if (text != null)
            validateStringNotBlank(text, "text");
        if (rangeStart != null)
            validateDateTimeFormat(rangeStart, "rangeStart");
        if (rangeEnd != null)
            validateDateTimeFormat(rangeEnd, "rangeEnd");
        validateSortOptions(sort);
        validatePageableParams(from, size);
        return service.getEventFiltered(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(
            @PathVariable("id") Long eventId) {
        validateNotNullObject(eventId, "id");
        validatePositiveNumber(eventId, "id");
        return service.getEventById(eventId);
    }
}
