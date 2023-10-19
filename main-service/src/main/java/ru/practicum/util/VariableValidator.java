package ru.practicum.util;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.event.dto.enums.EventAdminStateAction;
import ru.practicum.event.dto.enums.EventUserStateAction;
import ru.practicum.event.dto.enums.SortOption;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.participation.model.ParticipationRequestStatus;
import ru.practicum.util.exception.OperationForbiddenException;
import ru.practicum.util.exception.RequiredArgsMissingException;
import ru.practicum.util.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VariableValidator {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void validateNotNullObject(Object var, String varName) {
        if (var == null)
            throw new RequiredArgsMissingException(
                    "Required argument is missing",
                    "Параметр '" + varName + "' отсутствует");
    }

    public static <T extends Number> void validatePositiveNumber(T var, String varName) {
        if (var.doubleValue() <= 0)
            throw new ValidationException(
                    "Invalid request argument",
                    "Параметр '" + varName + "' должен быть положительным");
    }

    public static <T extends Number> void validateNotNegativeNumber(T var, String varName) {
        if (var.doubleValue() < 0)
            throw new ValidationException(
                    "Invalid request argument",
                    "Параметр '" + varName + "' не может быть отрицательным");
    }

    public static void validatePageableParams(int from, int size) {
        if (from < 0)
            throw new ValidationException("Invalid page params", "Параметр 'from' не может быть отрицательным");
        if (size < 1)
            throw new ValidationException("Invalid page params", "Параметр 'size' должен быть положительным");
    }

    public static void validateStringNotBlank(String var, String varName) {
        if (StringUtils.isBlank(var))
            throw new ValidationException("Incorrectly made request",
                    "Field: " + varName + ". Error: must not be blank. Value: null");
    }

    public static void validateStringLength(String var, String varName, int minValue, int maxValue) {
        if (var.length() < minValue || var.length() > maxValue)
            throw new ValidationException("Incorrectly made request",
                    "Field: " + varName + ". Error: должно содержать от " + minValue + " до " + maxValue + " символов. "
                            + "Value: " + var);
    }

    public static void validateDateTimeFormat(String var, String varName) {
        try {
            LocalDateTime.parse(var, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new ValidationException("Incorrectly made request",
                    "Field: " + varName + ". Error: должно быть в формате " + DATE_TIME_FORMATTER + ". "
                            + "Value: " + var);
        }
    }

    public static void validateFutureDateTime(LocalDateTime var, String varName, int timeInterval) {
        LocalDateTime now = LocalDateTime.now();
        if (var.isBefore(now))
            throw new ValidationException("For the requested operation the conditions are not met",
                    "Field: " + varName + ". Error: должно содержать дату, которая еще не наступила. "
                            + "Value:" + var);
        if (var.isBefore(now.plusHours(timeInterval)))
            throw new ValidationException("For the requested operation the conditions are not met",
                    "Field: " + varName + ". Error: должно содержать время не раньше чем через "
                            + timeInterval + " часа. Value: " + var);
    }

    public static void validateLocation(Location location) {
        validateNotNullObject(location.getLat(), "lat");
        validateNotNullObject(location.getLon(), "lon");
    }

    public static void validateEventState(String value) {
        if (!EventState.isValid(value))
            throw new OperationForbiddenException("For the requested operation the conditions are not met",
                    "Field: state. Error: значение статуса может быть: PENDING, PUBLISHED, REJECTED, CANCELED. "
                            + "Value:" + value);
    }

    public static void validateEventUserStateAction(String value) {
        if (!EventUserStateAction.isValid(value))
            throw new OperationForbiddenException("For the requested operation the conditions are not met",
                    "Field: stateAction. Error: значение статуса может быть: SEND_TO_REVIEW, CANCEL_REVIEW. "
                            + "Value:" + value);
    }

    public static void validateEventAdminStateAction(String value) {
        if (!EventAdminStateAction.isValid(value))
            throw new OperationForbiddenException("For the requested operation the conditions are not met",
                    "Field: stateAction. Error: значение статуса может быть: PUBLISH_EVENT, REJECT_EVENT. "
                            + "Value:" + value);
    }

    public static void validateParticipationRequestStatus(String value) {
        if (!ParticipationRequestStatus.isValid(value))
            throw new OperationForbiddenException("For the requested operation the conditions are not met",
                    "Field: status. Error: значение статуса может быть: CONFIRMED, REJECTED. "
                            + "Value:" + value);
    }

    public static void validateSortOptions(String value) {
        if (!SortOption.isValid(value))
            throw new OperationForbiddenException("For the requested operation the conditions are not met",
                    "Field: sort. Error: значение статуса может быть: EVENT_DATE, VIEWS. "
                            + "Value:" + value);
    }
}
