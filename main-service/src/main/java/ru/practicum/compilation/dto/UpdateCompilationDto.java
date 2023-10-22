package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationDto {
    private List<Long> events;

    private Long id;

    private Boolean pinned;

    private String title;
}