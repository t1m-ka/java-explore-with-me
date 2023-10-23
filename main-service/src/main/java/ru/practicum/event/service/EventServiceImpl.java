package ru.practicum.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.repository.CommentRepository;
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

import static ru.practicum.event.dto.EventDtoValidator.*;
import static ru.practicum.event.dto.EventMapper.toEventFullDto;
import static ru.practicum.event.dto.enums.EventAdminStateAction.PUBLISH_EVENT;
import static ru.practicum.event.dto.enums.EventAdminStateAction.REJECT_EVENT;
import static ru.practicum.event.dto.enums.EventUserStateAction.CANCEL_REVIEW;
import static ru.practicum.event.dto.enums.EventUserStateAction.SEND_TO_REVIEW;
import static ru.practicum.event.model.EventState.*;
import static ru.practicum.participation.dto.ParticipationMapper.toParticipationRequestDto;
import static ru.practicum.util.PageParamsMaker.makePageable;
import static ru.practicum.util.PageParamsMaker.makePageableWithSort;
import static ru.practicum.util.VariableValidator.*;
import static ru.practicum.util.VariableValidator.validatePageableParams;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final HitClient hitClient;
    private final StatsClient statsClient;

    public EventServiceImpl(EventRepository eventRepository,
            UserRepository userRepository,
            ParticipationRepository participationRepository,
            CategoryRepository categoryRepository,
            CommentRepository commentRepository,
            @Value("${stats-service.url}") String statServiceUrl,
            RestTemplateBuilder builder,
            ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participationRepository = participationRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.hitClient = new HitClient(statServiceUrl, builder, objectMapper);
        this.statsClient = new StatsClient(statServiceUrl, builder, objectMapper);
    }

    @Override
    public List<EventShortDto> getUserEventList(Long userId, int from, int size) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validatePageableParams(from, size);
        findUser(userId);
        List<Event> initiatorEventList = eventRepository.findAllByInitiatorId(userId, makePageable(from, size));
        return initiatorEventList
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNewEvent(newEventDto);
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
        return toEventFullDto(eventRepository.save(newEvent), new ArrayList<>());
    }

    @Transactional
    @Override
    public EventFullDto getUserEventFullByEventId(Long userId, Long eventId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        findUser(userId);
        Event event = findEvent(eventId);

        if (event.getInitiator().getId() != userId)
            throw new RequestConflictException("Invalid operation",
                    "Просмотр информации разрешен только инициатору события.");
        event.setViews(event.getViews() + 1);
        List<CommentFullDto> commentList = findEventCommentsDto(eventId);
        return toEventFullDto(event, commentList);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest updateEventDto) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        validateUpdateEvent(updateEventDto, false);
        findUser(userId);
        Event event = findEvent(eventId);
        if (event.getInitiator().getId() != userId)
            throw new RequestConflictException("Invalid operation",
                    "Изменение события разрешено только инициатору события.");
        if (event.getState() == EventState.PUBLISHED)
            throw new RequestConflictException("Invalid operation",
                    "Событие уже опубликовано, редактирование запрещено.");

        changeEvent(event, updateEventDto, false);
        List<CommentFullDto> commentList = findEventCommentsDto(eventId);
        return toEventFullDto(event, commentList);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventParticipationRequests(Long userId, Long eventId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        findUser(userId);
        findEvent(eventId);
        return participationRepository.findAllRequestsByEventIdAndInitiatorId(userId, eventId).stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeParticipationStatus(Long userId, Long eventId,
            EventRequestStatusUpdateRequest statusUpdateRequest) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        validateNotNullObject(statusUpdateRequest, "EventRequestStatusUpdateRequest");
        validateUpdateRequestStatus(statusUpdateRequest);
        User initiator = findUser(userId);
        Event event = findEvent(eventId);
        if (initiator.getId() != event.getInitiator().getId())
            throw new RequestConflictException("Invalid operation",
                    "Одобрять запросы на участие может только инициатор события.");

        List<ParticipationRequest> requestList =
                participationRepository.findRequestsByIdsList(eventId, statusUpdateRequest.getRequestIds());
        requestList.forEach(request -> {
            if (!request.getRequestStatus().equals(ParticipationRequestStatus.PENDING))
                throw new RequestConflictException("Invalid operation",
                        "Запрос с id=" + request.getId() + " имеет статус '" + request.getRequestStatus()
                                + "'. Изменение невозможно.");
        });

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        ParticipationRequestStatus newStatus;
        if (statusUpdateRequest.getStatus().equals(ParticipationRequestStatus.REJECTED.name())) {
            newStatus = ParticipationRequestStatus.REJECTED;
            for (ParticipationRequest request : requestList) {
                request.setRequestStatus(newStatus);
                rejectedRequests.add(toParticipationRequestDto(request));
            }
        } else {
            newStatus = ParticipationRequestStatus.CONFIRMED;
            for (ParticipationRequest request : requestList) {
                Event requestedEvent = request.getEvent();
                if (requestedEvent.getConfirmedRequests() < requestedEvent.getParticipantLimit()) {
                    request.setRequestStatus(newStatus);
                    requestedEvent.setConfirmedRequests(requestedEvent.getConfirmedRequests() + 1);
                    confirmedRequests.add(toParticipationRequestDto(request));
                } else {
                    request.setRequestStatus(ParticipationRequestStatus.REJECTED);
                    throw new RequestConflictException("Invalid operation",
                            "Достигнут лимит участников события с id=" + requestedEvent.getId() + ".");
                }
            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<String> states, List<Long> categories,
            String rangeStart, String rangeEnd, int from, int size) {
        if (users != null) {
            users.forEach(x -> validateNotNullObject(x, "users"));
            users.forEach(x -> validatePositiveNumber(x, "users"));
        }
        if (categories != null) {
            categories.forEach(x -> validateNotNullObject(x, "categories"));
            categories.forEach(x -> validatePositiveNumber(x, "categories"));
        }
        if (rangeStart != null)
            validateDateTimeFormat(rangeStart, "rangeStart");
        if (rangeEnd != null)
            validateDateTimeFormat(rangeEnd, "rangeEnd");
        validatePageableParams(from, size);

        LocalDateTime start = null;
        LocalDateTime end = null;
        List<EventState> eventStatesList = null;
        if (states != null && !states.isEmpty()) {
            states.forEach(VariableValidator::validateEventState);
            eventStatesList = states.stream()
                    .map(EventState::fromString)
                    .collect(Collectors.toList());
        }

        if (!StringUtils.isBlank(rangeStart))
            start = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        if (!StringUtils.isBlank(rangeEnd))
            end = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);

        return eventRepository.searchEventsByAdmin(users, eventStatesList, categories,
                        start, end, makePageable(from, size)).stream()
                .map(event -> {
                    List<CommentFullDto> commentList = findEventCommentsDto(event.getId());
                    return EventMapper.toEventFullDto(event, commentList);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updateEventDto) {
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        validateNotNullObject(updateEventDto, "UpdateEventAdminRequest");
        validateUpdateEvent(updateEventDto, true);
        Event event = findEvent(eventId);
        changeEvent(event, updateEventDto, true);
        List<CommentFullDto> commentList = findEventCommentsDto(event.getId());
        return toEventFullDto(event, commentList);
    }

    @Override
    public List<EventShortDto> getEventFiltered(HttpServletRequest request, String text, List<Long> categories,
            Boolean paid, String rangeStart, String rangeEnd, boolean onlyAvailable,
            String sort, int from, int size) {
        if (text != null)
            validateStringNotBlank(text, "text");
        if (categories != null) {
            categories.forEach(x -> validateNotNullObject(x, "categories"));
            categories.forEach(x -> validatePositiveNumber(x, "categories"));
        }
        if (rangeStart != null)
            validateDateTimeFormat(rangeStart, "rangeStart");
        if (rangeEnd != null)
            validateDateTimeFormat(rangeEnd, "rangeEnd");
        if (sort != null)
            validateSortOptions(sort);
        validatePageableParams(from, size);

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
    public EventFullDto getEventById(HttpServletRequest request, Long eventId) {
        validateNotNullObject(eventId, "id");
        validatePositiveNumber(eventId, "id");
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
        List<CommentFullDto> commentList = findEventCommentsDto(event.getId());
        return toEventFullDto(event, commentList);
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

    private List<CommentFullDto> findEventCommentsDto(long eventId) {
        return commentRepository.findAllByEventIdOrderByCreatedOn(eventId).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }
}
