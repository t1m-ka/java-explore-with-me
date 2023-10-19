package ru.practicum.compilation.dto;

import static ru.practicum.util.VariableValidator.validateStringLength;
import static ru.practicum.util.VariableValidator.validateStringNotBlank;

public class CompilationValidator {
    private static final int MIN_TITLE_LENGTH = 1;
    private static final int MAX_TITLE_LENGTH = 50;

    public static void validateNewCompilationDto(NewCompilationDto newCompilationDto) {
        validateStringNotBlank(newCompilationDto.getTitle(), "title");
        validateStringLength(newCompilationDto.getTitle(), "title", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH);
    }

    public static void validateUpdateCompilationDto(UpdateCompilationDto updateCompilationDto) {
        if (updateCompilationDto.getTitle() != null)
            validateStringLength(updateCompilationDto.getTitle(), "title", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH);
    }
}
