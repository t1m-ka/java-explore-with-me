package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.dto.ModeratedCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.ModerationReason;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.EntityNotFoundException;
import ru.practicum.util.exception.RequestConflictException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.comment.dto.CommentDtoValidator.validateCommentDto;
import static ru.practicum.comment.dto.CommentDtoValidator.validateModeratedCommentDto;
import static ru.practicum.comment.dto.CommentMapper.toCommentFullDto;
import static ru.practicum.util.PageParamsMaker.makePageable;
import static ru.practicum.util.VariableValidator.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentFullDto> getEventComments(Long eventId, String rangeStart, String rangeEnd,
            int from, int size) {
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            validateDateTimeFormat(rangeStart, "rangeStart");
            start = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        }
        if (rangeEnd != null) {
            validateDateTimeFormat(rangeEnd, "rangeEnd");
            end = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);
        }
        validatePageableParams(from, size);

        return commentRepository.findEventCommentsByFilter(eventId, start, end, makePageable(from, size)).stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentFullDto getCommentById(Long commentId) {
        validateNotNullObject(commentId, "commentId");
        validatePositiveNumber(commentId, "commentId");
        return toCommentFullDto(findComment(commentId));
    }

    @Override
    public List<CommentFullDto> searchUserComments(Long userId, Long eventId, String rangeStart, String rangeEnd,
            int from, int size) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        if (eventId != null)
            validatePositiveNumber(eventId, "eventId");
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            validateDateTimeFormat(rangeStart, "rangeStart");
            start = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        }
        if (rangeEnd != null) {
            validateDateTimeFormat(rangeEnd, "rangeEnd");
            end = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);
        }
        validatePageableParams(from, size);

        return commentRepository.findUserCommentsByFilter(userId, eventId, start, end, makePageable(from, size))
                .stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentFullDto createComment(Long userId, Long eventId, CommentDto commentDto) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(eventId, "eventId");
        validatePositiveNumber(eventId, "eventId");
        validateCommentDto(commentDto);
        User author = findUser(userId);
        Event event = findEvent(eventId);
        if (event.getState() != EventState.PUBLISHED)
            throw new RequestConflictException("Invalid operation",
                    "Событие еще не опубликовано.");
        Comment newComment = Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(author)
                .createdOn(LocalDateTime.now())
                .build();
        return toCommentFullDto(commentRepository.save(newComment));
    }

    @Transactional
    @Override
    public CommentFullDto updateComment(Long userId, Long commentId, CommentDto commentDto) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(commentId, "commentId");
        validatePositiveNumber(commentId, "commentId");
        validateCommentDto(commentDto);
        User user = findUser(userId);
        Comment comment = findComment(commentId);
        if (comment.getAuthor().getId() != user.getId())
            throw new RequestConflictException("Invalid operation",
                    "Редактировать комментарий может только его автор.");
        comment.setText(commentDto.getText());
        comment.setUpdatedOn(LocalDateTime.now());
        return toCommentFullDto(comment);
    }

    @Override
    public void deleteCommentByAuthor(Long userId, Long commentId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        validateNotNullObject(commentId, "commentId");
        validatePositiveNumber(commentId, "commentId");
        User user = findUser(userId);
        Comment comment = findComment(commentId);
        if (comment.getAuthor().getId() != user.getId())
            throw new RequestConflictException("Invalid operation",
                    "Удалить комментарий может только его автор.");
        commentRepository.delete(comment);
    }

    @Transactional
    @Override
    public CommentFullDto moderateComment(Long commentId, ModeratedCommentDto moderatedCommentDto) {
        validateNotNullObject(commentId, "commentId");
        validatePositiveNumber(commentId, "commentId");
        validateModeratedCommentDto(moderatedCommentDto);
        Comment comment = findComment(commentId);
        comment.setText(moderatedCommentDto.getText());
        comment.setModerationReason(ModerationReason.valueOf(moderatedCommentDto.getModerationReason()));
        comment.setModerationOn(LocalDateTime.now());
        return toCommentFullDto(comment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        validateNotNullObject(commentId, "commentId");
        validatePositiveNumber(commentId, "commentId");
        Comment comment = findComment(commentId);
        commentRepository.delete(comment);
    }

    private Comment findComment(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Comment with id=" + commentId + "was not found"));
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
}
