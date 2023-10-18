package ru.practicum.compilation.dto;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getEvents().stream()
                        .map(Event::getId)
                        .collect(Collectors.toList()),
                compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle()
        );
    }

    public static Compilation toCompilation(CompilationDto compilationDto, List<Event> events) {
        return new Compilation(
                compilationDto.getId(),
                compilationDto.getPinned(),
                compilationDto.getTitle(),
                new HashSet<>(events)
        );
    }
}
