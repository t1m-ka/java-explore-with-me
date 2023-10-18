package ru.practicum.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.dto.enums.SortOption;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.hit.HitClient;
import ru.practicum.hit.HitDto;
import ru.practicum.participation.dto.ParticipationMapper;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.participation.model.ParticipationRequest;
import ru.practicum.participation.model.ParticipationRequestStatus;
import ru.practicum.participation.repository.ParticipationRepository;
import ru.practicum.stats.StatsClient;
import ru.practicum.stats.StatsDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.VariableValidator;
import ru.practicum.util.exception.EntityNotFoundException;
import ru.practicum.util.exception.RequestConflictException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.event.dto.EventMapper.toEventFullDto;
import static ru.practicum.event.dto.enums.EventAdminStateAction.PUBLISH_EVENT;
import static ru.practicum.event.dto.enums.EventAdminStateAction.REJECT_EVENT;
import static ru.practicum.event.dto.enums.EventUserStateAction.CANCEL_REVIEW;
import static ru.practicum.event.dto.enums.EventUserStateAction.SEND_TO_REVIEW;
import static ru.practicum.event.model.EventState.*;
import static ru.practicum.participation.dto.ParticipationMapper.toParticipationRequestDto;
import static ru.practicum.util.PageParamsMaker.makePageable;
import static ru.practicum.util.PageParamsMaker.makePageableWithSort;
import static ru.practicum.util.VariableValidator.DATE_TIME_FORMATTER;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final CategoryRepository categoryRepository;
    private final HitClient hitClient;
    private final StatsClient statsClient;

    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            ParticipationRepository participationRepository,
                            CategoryRepository categoryRepository,
                            @Value("${stats-service.url}") String statServiceUrl,
                            RestTemplateBuilder builder,
                            ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participationRepository = participationRepository;
        this.categoryRepository = categoryRepository;
        this.hitClient = new HitClient(statServiceUrl, builder, objectMapper);
        this.statsClient = new StatsClient(statServiceUrl, builder, objectMapper);
    }

    @Override
    public List<EventShortDto> getUserEventList(long userId, int from, int size) {
        findUser(userId);
        List<Event> initiatorEventList = eventRepository.findAllByInitiatorId(userId, makePageable(from, size));
        return initiatorEventList
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        User initiator = findUser(userId);
        Category category = findCategory(newEventDto.getCategory());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), DATE_TIME_FORMATTER);
        long views = 0;
        int confirmedRequests = 0;
        Event newEvent = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedRequests)
                .description(newEventDto.getDescription())
                .createdOn(now)
                .eventDate(eventDate)
                .initiator(initiator)
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(PENDING)
                .title(newEventDto.getTitle())
                .views(views)
                .build();
        return toEventFullDto(eventRepository.save(newEvent));
    }

    @Transactional
    @Override
    public EventFullDto getUserEventFullByEventId(long userId, long eventId) {
        findUser(userId);
        Event event = findEvent(eventId);

        if (event.getInitiator().getId() != userId)
            throw new RequestConflictException("Invalid operation",
                    "Просмотр информации разрешен только инициатору события.");
        event.setViews(event.getViews() + 1);
        return toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByUser(long userId, long eventId, UpdateEventRequest updateEventDto) {
        findUser(userId);
        Event event = findEvent(eventId);
        if (event.getInitiator().getId() != userId)
            throw new RequestConflictException("Invalid operation",
                    "Изменение события разрешено только инициатору события.");
        if (event.getState() == EventState.PUBLISHED)
            throw new RequestConflictException("Invalid operation",
                    "Событие уже опубликовано, редактирование запрещено.");

        changeEvent(event, updateEventDto, false);
        return toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventParticipationRequests(long userId, long eventId) {
        findUser(userId);
        findEvent(eventId);
        return participationRepository.findAllRequestsByEventIdAndInitiatorId(userId, eventId).stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeParticipationStatus(long userId, long eventId,
                                                                    EventRequestStatusUpdateRequest statusUpdateRequest) {
        findUser(userId);
        findEvent(eventId);
        List<ParticipationRequest> requestList =
                participationRepository.findUserRequestsByEventIdsList(userId, statusUpdateRequest.getRequestIds());
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (ParticipationRequest request : requestList) {
            Event event = request.getEvent();

            if (!request.getRequestStatus().equals(ParticipationRequestStatus.PENDING))
                throw new RequestConflictException("Invalid operation",
                        "Запрос с id=" + request.getId() + " имеет статус '" + request.getRequestStatus() + "'.");
            if (statusUpdateRequest.getStatus().equals(ParticipationRequestStatus.REJECTED.name())) {
                request.setRequestStatus(ParticipationRequestStatus.REJECTED);
                rejectedRequests.add(toParticipationRequestDto(request));
            } else {
                if (event.getParticipantLimit() == 0 || event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setRequestStatus(ParticipationRequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    confirmedRequests.add(toParticipationRequestDto(request));
                } else {
                    request.setRequestStatus(ParticipationRequestStatus.REJECTED);
                    rejectedRequests.add(toParticipationRequestDto(request));
                }
            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<String> states, List<Long> categories,
                                           String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        states.forEach(VariableValidator::validateEventState);
        List<EventState> eventStatesList = states.stream()
                .map(EventState::fromString)
                .collect(Collectors.toList());

        if (!StringUtils.isBlank(rangeStart))
            start = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        if (!StringUtils.isBlank(rangeEnd))
            end = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);

        return eventRepository.searchEventsByAdmin(users, eventStatesList, categories,
                        start, end, makePageable(from, size)).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(long eventId, UpdateEventRequest updateEventDto) {
        Event event = findEvent(eventId);
        changeEvent(event, updateEventDto, true);
        return toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventFiltered(HttpServletRequest request, String text, List<Long> categories,
                                                Boolean paid, String rangeStart, String rangeEnd, boolean onlyAvailable,
                                                String sort, int from, int size) {
        makeStatsHit(request);
        Sort sortParam;
        Pageable pageable;
        if (sort != null) {
            if (sort.equals(SortOption.EVENT_DATE.name()))
                sortParam = Sort.by(Sort.Direction.ASC, "eventDate");
            else
                sortParam = Sort.by(Sort.Direction.ASC, "views");
            pageable = makePageableWithSort(from, size, sortParam);
        } else {
            pageable = makePageable(from, size);
        }

        LocalDateTime start = null;
        LocalDateTime end = null;
        if (!StringUtils.isBlank(rangeStart))
            start = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        if (!StringUtils.isBlank(rangeEnd))
            end = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);

        return eventRepository.getEventFiltered(text, paid, onlyAvailable, categories, start, end, pageable)
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto getEventById(HttpServletRequest request, long eventId) {
        makeStatsHit(request);
        Event event = eventRepository.findPublishedEvent(eventId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Published event with id=" + eventId + "was not found"));

        List<StatsDto> statsList = statsClient.getStats(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                Collections.singletonList(request.getRequestURI()),
                true);
        event.setViews(statsList.size());
        return toEventFullDto(event);
    }

    private void makeStatsHit(HttpServletRequest request) {
        HitDto hitDto = new HitDto();
        hitDto.setApp("ewm-main-service");
        hitDto.setUri(request.getRequestURI());
        hitDto.setIp(request.getRemoteAddr());
        hitDto.setTimestamp(LocalDateTime.now());
        hitClient.saveHit(hitDto);
    }

    private User findUser(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "User with id=" + userId + "was not found"));
    }

    private Event findEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Event with id=" + eventId + "was not found"));
    }

    private Category findCategory(long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Category with id=" + catId + "was not found"));
    }

    private void changeEvent(Event event, UpdateEventRequest updateEventDto, boolean isAdmin) {
        if (updateEventDto.getAnnotation() != null)
            event.setAnnotation(updateEventDto.getAnnotation());
        if (updateEventDto.getCategoryId() != null)
            event.setCategory(findCategory(updateEventDto.getCategoryId()));
        if (updateEventDto.getDescription() != null)
            event.setDescription(updateEventDto.getDescription());
        if (updateEventDto.getEventDate() != null)
            event.setEventDate(LocalDateTime.parse(updateEventDto.getEventDate(), DATE_TIME_FORMATTER));
        if (updateEventDto.getLocation() != null) {
            event.setLat(updateEventDto.getLocation().getLat());
            event.setLon(updateEventDto.getLocation().getLon());
        }
        if (updateEventDto.getPaid() != null)
            event.setPaid(updateEventDto.getPaid());
        if (updateEventDto.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        if (updateEventDto.getRequestModeration() != null)
            event.setRequestModeration(updateEventDto.getRequestModeration());

        if (updateEventDto.getStateAction() != null) {
            if (isAdmin) {
                if (updateEventDto.getStateAction().equals(PUBLISH_EVENT.name()) && event.getState() == PENDING) {
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else if (updateEventDto.getStateAction().equals(REJECT_EVENT.name()) && event.getState() == PENDING) {
                    event.setState(REJECTED);
                } else {
                    throw new RequestConflictException("Invalid operation",
                            "Выполнить операцию можно если событие в состоянии ожидания публикации.");
                }
            } else {
                if (updateEventDto.getStateAction().equals(SEND_TO_REVIEW.name()))
                    event.setState(PENDING);
                if (updateEventDto.getStateAction().equals(CANCEL_REVIEW.name()))
                    event.setState(EventState.CANCELED);
            }
        }
        if (updateEventDto.getTitle() != null)
            event.setTitle(updateEventDto.getTitle());
    }
}
