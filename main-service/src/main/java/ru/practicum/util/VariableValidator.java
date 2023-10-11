package ru.practicum.util;

import ru.practicum.util.exception.RequiredArgsMissingException;
import ru.practicum.util.exception.ValidationException;

public class VariableValidator {
    public static void validateNotNullVariable(Long var, String varName) {
        if (var == null)
            throw new RequiredArgsMissingException(
                    "Required argument is missing",
                    "Path variable '" + varName + "' is missing");
    }

    public static void validatePositiveVariable(Long var, String varName) {
        if (var <= 0)
            throw new ValidationException(
                    "Invalid request argument",
                    "Path variable '" + varName + "' should be positive");
    }

    public static void validatePageableParams(int from, int size) {
        if (from < 0)
            throw new ValidationException("Invalid page params", "Param 'from' should be positive or zero");
        if (size < 1)
            throw new ValidationException("Invalid page params", "Param 'size' should be positive");
    }
}
