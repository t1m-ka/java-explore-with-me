package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CompilationDto {
    private List<Long> events;

    private Boolean pinned = false;

    private String title;
}
