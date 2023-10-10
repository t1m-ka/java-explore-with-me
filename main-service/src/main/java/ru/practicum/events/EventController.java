package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.*;
import ru.practicum.events.service.EventService;
import ru.practicum.participations.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService service;

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getUserEventList(
            @PathVariable long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return null;
    }

    @PostMapping("/users/{userId}/events") // условия в спецификации
    public EventFullDto createEvent(
            @PathVariable long userId,
            @RequestBody NewEventDto newEventDto) {
        return null;
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getUserEventFullByEventId(
            @PathVariable long userId,
            @PathVariable long eventId) {
        return null;
    }

    @PatchMapping("/users/{userId}/events/{eventId}") // условия в спецификации
    public EventFullDto updateEventByUser(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody UpdateEventUserRequest updateEventDto) {
        return null;
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventParticipationRequests(
            @PathVariable long userId,
            @PathVariable long eventId) {
        return null;
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests") // условия в спецификации
    public EventRequestStatusUpdateResult changeParticipationStatus(
            @PathVariable long userId,
            @PathVariable long eventId,
            @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        return null;
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
        return null;
    }

    @PatchMapping("/admin/events/{eventId}") // условия в спецификации
    public EventFullDto updateEventByAdmin(
            @PathVariable long eventId,
            @RequestBody UpdateEventAdminRequest updateEventDto) {
        return null;
    }

    @GetMapping("/events") // условия в спецификации
    public List<EventShortDto> getEventFiltered(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return null;
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(
            @PathVariable("id") long eventId) {
        return null;
    }
}
