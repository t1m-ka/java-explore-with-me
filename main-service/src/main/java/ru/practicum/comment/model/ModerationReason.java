package ru.practicum.comment.model;

public enum ModerationReason {
    //оскорбление
    PROFANITY,
    //спам
    SPAM,
    //реклама
    ADVERTISEMENT,
    //неприемлимый контент
    UNACCEPTABLE_CONTENT,
    //недостоверная информация
    INCORRECT_INFORMATION;

    public static boolean isValid(String value) {
        for (ModerationReason reasons : values()) {
            if (reasons.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
