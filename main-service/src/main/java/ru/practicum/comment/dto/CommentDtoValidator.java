package ru.practicum.comment.dto;

import static ru.practicum.util.VariableValidator.*;

public class CommentDtoValidator {
    private static final int MIN_TEXT_LENGTH = 10;
    private static final int MAX_TEXT_LENGTH = 1000;

    public static void validateCommentDto(CommentDto commentDto) {
        validateStringNotBlank(commentDto.getText(), "text");
        validateStringLength(commentDto.getText(), "text", MIN_TEXT_LENGTH, MAX_TEXT_LENGTH);
    }

    public static void validateModeratedCommentDto(ModeratedCommentDto moderatedCommentDto) {
        validateStringNotBlank(moderatedCommentDto.getText(), "text");
        validateStringLength(moderatedCommentDto.getText(), "text", MIN_TEXT_LENGTH, MAX_TEXT_LENGTH);
        validateModerationReason(moderatedCommentDto.getModerationReason());
    }
}
