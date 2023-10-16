package ru.practicum.category.dto;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.util.exception.ValidationException;

public class CategoryDtoValidator {
    public static void validateNewCategory(CategoryDto categoryDto) {
        if (StringUtils.isBlank(categoryDto.getName()))
            throw new ValidationException("Incorrectly made request", "The 'name' field must not be blank");
        if (categoryDto.getName().length() > 50)
            throw new ValidationException("Incorrectly made request",
                    "Length of field 'name' should not exceed 50 chars");
    }
}
