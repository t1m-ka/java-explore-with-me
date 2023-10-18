package ru.practicum.event.dto;

import java.time.LocalDateTime;

import static ru.practicum.util.VariableValidator.*;
import static ru.practicum.util.VariableValidator.validateNotNullObject;

public class EventDtoValidator {
    private static final int MIN_ANNOTATION_LENGTH = 20;
    private static final int MAX_ANNOTATION_LENGTH = 2000;
    private static final int MIN_DESCRIPTION_LENGTH = 20;
    private static final int MAX_DESCRIPTION_LENGTH = 7000;
    private static final int MIN_HOURS_BEFORE_EVENT_USER = 2;
    private static final int MIN_HOURS_BEFORE_EVENT_ADMIN = 1;
    private static final int MIN_TITLE_LENGTH = 3;
    private static final int MAX_TITLE_LENGTH = 120;

    public static void validateNewEvent(NewEventDto eventDto) {
        validateStringNotBlank(eventDto.getAnnotation(), "annotation");
        validateStringLength(eventDto.getAnnotation(), "annotation", MIN_ANNOTATION_LENGTH, MAX_ANNOTATION_LENGTH);

        validateNotNullObject(eventDto.getCategory(), "category");
        validatePositiveNumber(eventDto.getCategory(), "category");

        validateStringNotBlank(eventDto.getDescription(), "description");
        validateStringLength(eventDto.getDescription(), "description", MIN_DESCRIPTION_LENGTH, MAX_DESCRIPTION_LENGTH);

        validateNotNullObject(eventDto.getEventDate(), "eventDate");
        validateDateTimeFormat(eventDto.getEventDate(), "eventDate");
        validateFutureDateTime(
                LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMATTER),
                "eventDate",
                MIN_HOURS_BEFORE_EVENT_USER);

        validateNotNullObject(eventDto.getLocation(), "location");
        validateLocation(eventDto.getLocation());

        if (eventDto.getParticipantLimit() != null)
            validateNotNegativeNumber(eventDto.getParticipantLimit(), "participantLimit");

        validateStringNotBlank(eventDto.getTitle(), "title");
        validateStringLength(eventDto.getTitle(), "title", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH);
    }

    public static void validateUpdateEvent(UpdateEventRequest eventDto, boolean isAdmin) {
        if (eventDto.getAnnotation() != null)
            validateStringLength(eventDto.getAnnotation(), "annotation", MIN_ANNOTATION_LENGTH, MAX_ANNOTATION_LENGTH);
        if (eventDto.getCategoryId() != null)
            validatePositiveNumber(eventDto.getCategoryId(), "category");
        if (eventDto.getDescription() != null)
            validateStringLength(eventDto.getDescription(), "description", MIN_DESCRIPTION_LENGTH,
                    MAX_DESCRIPTION_LENGTH);

        if (eventDto.getEventDate() != null) {
            validateDateTimeFormat(eventDto.getEventDate(), "eventDate");
            if (isAdmin)
                validateFutureDateTime(
                        LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMATTER),
                        "eventDate",
                        MIN_HOURS_BEFORE_EVENT_ADMIN);
            else
                validateFutureDateTime(
                        LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMATTER),
                        "eventDate",
                        MIN_HOURS_BEFORE_EVENT_USER);
        }

        if (eventDto.getLocation() != null)
            validateLocation(eventDto.getLocation());

        if (eventDto.getParticipantLimit() != null)
            validateNotNegativeNumber(eventDto.getParticipantLimit(), "participantLimit");

        if (eventDto.getStateAction() != null)
            if (isAdmin)
                validateEventAdminStateAction(eventDto.getStateAction());
            else
                validateEventUserStateAction(eventDto.getStateAction());

        if (eventDto.getTitle() != null)
            validateStringLength(eventDto.getTitle(), "title", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH);
    }

    public static void validateUpdateRequestStatus(EventRequestStatusUpdateRequest statusUpdateRequest) {
        validateNotNullObject(statusUpdateRequest.getRequestIds(), "requestIds");
        validateParticipationRequestStatus(statusUpdateRequest.getStatus());
    }
}
