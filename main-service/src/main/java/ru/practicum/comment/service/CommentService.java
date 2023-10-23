package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.ModeratedCommentDto;

import java.util.List;

public interface CommentService {
    List<CommentFullDto> getEventComments(Long eventId, String rangeStart, String rangeEnd, int from, int size);

    CommentFullDto getCommentById(Long commentId);

    List<CommentFullDto> searchUserComments(Long userId, Long eventId, String rangeStart, String rangeEnd, int from, int size);

    CommentFullDto createComment(Long userId, Long eventId, CommentDto commentDto);

    CommentFullDto updateComment(Long userId, Long commentId, CommentDto commentDto);

    void deleteCommentByAuthor(Long userId, Long commentId);

    CommentFullDto moderateComment(Long commentId, ModeratedCommentDto moderatedCommentDto);

    void deleteCommentByAdmin(Long commentId);
}
