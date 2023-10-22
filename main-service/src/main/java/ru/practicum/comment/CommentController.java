package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.ModeratedCommentDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    //публичный эндпоинт для получения комментариев по событию
    @GetMapping("/events/{eventId}/comments")
    public List<CommentFullDto> getEventComments(
            @PathVariable Long eventId,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return service.getEventComments(eventId, rangeStart, rangeEnd, from, size);
    }

    //публичный эндпоинт для получения конкретного комментария
    @GetMapping("/comments/{commentId}")
    public CommentFullDto getCommentById(
            @PathVariable Long commentId) {
        return service.getCommentById(commentId);
    }

    //эндпоинт для авторизированных пользователей, позволяющий просмотреть все свои комментарии
    @GetMapping("/users/{userId}/comments")
    public List<CommentFullDto> searchUserComments(
            @PathVariable Long userId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return service.searchUserComments(userId, eventId, rangeStart, rangeEnd, from, size);
    }

    //эндпоинт для авторизированных пользователей, позволяющий создать комментарий для опубликованного события
    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody CommentDto commentDto) {
        return service.createComment(userId, eventId, commentDto);
    }

    //эндпоинт для авторизированных пользователей, позволяющий отредактировать свой комментарий
    @PatchMapping("/users/{userId}/comments/{commentId}")
    public CommentFullDto updateComment(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto) {
        return service.updateComment(userId, commentId, commentDto);
    }

    //эндпоинт для авторизированных пользователей, позволяющий удалить свой комментарий
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAuthor(
            @PathVariable Long userId,
            @PathVariable Long commentId) {
        service.deleteCommentByAuthor(userId, commentId);
    }

    //эндпоинт для администратора, позволяющий модерировать комментарий
    @PatchMapping("/admin/comments/{commentId}")
    public CommentFullDto moderateComment(
            @PathVariable Long commentId,
            @RequestBody ModeratedCommentDto moderatedCommentDto) {
        return service.moderateComment(commentId, moderatedCommentDto);
    }

    //эндпоинт для администратора, позволяющий удалить комментарий
    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(
            @PathVariable Long commentId) {
        service.deleteCommentByAdmin(commentId);
    }
}
