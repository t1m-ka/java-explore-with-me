package ru.practicum.comment.dto;

import ru.practicum.comment.model.Comment;

import static ru.practicum.user.dto.UserMapper.toUserShortDto;

public class CommentMapper {
    public static CommentFullDto toCommentFullDto(Comment comment) {
        return new CommentFullDto(
                comment.getId(),
                comment.getText(),
                comment.getEvent().getId(),
                toUserShortDto(comment.getAuthor()),
                comment.getCreatedOn(),
                comment.getUpdatedOn(),
                comment.getModerationReason(),
                comment.getModerationOn()
        );
    }
}
