package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;

    private Long id;

    private Boolean pinned;

    private String title;
}