package ru.practicum.participation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.participation.dto.ParticipationMapper;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.participation.model.ParticipationRequest;
import ru.practicum.participation.model.ParticipationRequestStatus;
import ru.practicum.participation.repository.ParticipationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.EntityAlreadyExistsException;
import ru.practicum.util.exception.EntityNotFoundException;
import ru.practicum.util.exception.RequestConflictException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.participation.dto.ParticipationMapper.toParticipationRequestDto;
import static ru.practicum.util.VariableValidator.validateNotNullObject;
import static ru.practicum.util.VariableValidator.validatePositiveNumber;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserParticipationRequestList(Long userId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "User with id=" + userId + "was not found"));
        return participationRepository.findAllByRequesterId(userId)
                .stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        User requester = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "User with id=" + userId + "was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Event with id=" + eventId + "was not found"));

        if (event.getInitiator().getId() == userId)
            throw new RequestConflictException("Invalid operation",
                    "Запрос на участие в событии не может быть подан инициатором.");
        if (event.getState() != EventState.PUBLISHED)
            throw new RequestConflictException("Invalid operation",
                    "Запрос на участие в событии не может быть подан пока событие не опубликовано.");

        List<ParticipationRequest> userParticipationRequests = participationRepository.findAllByRequesterId(userId);
        for (ParticipationRequest request : userParticipationRequests) {
            if (request.getEvent().getId() == eventId)
                throw new EntityAlreadyExistsException("Entity already exist",
                        "Запрос на участие в этом событии уже создан.");
        }
        int requestCount = participationRepository.findAllConfirmedRequestsByEventId(eventId).size();
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requestCount)
            throw new RequestConflictException("Invalid operation",
                    "Достигнут лимит запросов на участие в событии.");

        ParticipationRequestStatus newRequestStatus;
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            newRequestStatus = ParticipationRequestStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            newRequestStatus = ParticipationRequestStatus.PENDING;
        }

        ParticipationRequest createdRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .requestStatus(newRequestStatus)
                .build();

        return toParticipationRequestDto(participationRepository.save(createdRequest));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(requestId, "requestId");
        validatePositiveNumber(requestId, "requestId");
        ParticipationRequest participationRequest = participationRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Request with id=" + requestId + "was not found"));
        User requester = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "User with id=" + userId + "was not found"));

        if (participationRequest.getRequester().getId() != requester.getId())
            throw new RequestConflictException("Invalid operation",
                    "Отменить запрос на участие в событии может только создатель запроса.");
        if (participationRequest.getRequestStatus() == ParticipationRequestStatus.CANCELED)
            throw new RequestConflictException("Invalid operation",
                    "Запрос на участие в событии уже имеет статус 'CANCELED'.");
        if (participationRequest.getRequestStatus() == ParticipationRequestStatus.CONFIRMED) {
            Event event = participationRequest.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        }
        participationRequest.setRequestStatus(ParticipationRequestStatus.CANCELED);

        return toParticipationRequestDto(participationRepository.save(participationRequest));
    }
}
