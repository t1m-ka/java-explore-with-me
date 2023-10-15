package ru.practicum.compilation.dto;

import static ru.practicum.util.VariableValidator.validateStringLength;
import static ru.practicum.util.VariableValidator.validateStringNotBlank;

public class CompilationValidator {
    private static final int MIN_TITLE_LENGTH = 1;
    private static final int MAX_TITLE_LENGTH = 50;

    public static void validateNewCompilationDto(CompilationDto compilationDto) {
        validateStringNotBlank(compilationDto.getTitle(), "title");
        validateStringLength(compilationDto.getTitle(), "title", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH);
    }

    public static void validateUpdateCompilationDto(CompilationDto compilationDto) {
        if (compilationDto.getTitle() != null)
            validateStringLength(compilationDto.getTitle(), "title", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH);
    }
}
