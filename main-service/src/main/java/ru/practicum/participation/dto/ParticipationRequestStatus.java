package ru.practicum.participation.dto;

public enum ParticipationRequestStatus {
    //ожидание рассмотрения
    PENDING,
    //заявка одобрена
    CONFIRMED,
    //заявка отклонена
    REJECTED,
    //заявка отменена инициатором
    CANCELED;

    public static boolean isValid(String value) {
        for (ParticipationRequestStatus requestStatus : values()) {
            if (requestStatus.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
