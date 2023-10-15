package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilation(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(long compId);

    CompilationDto createCompilation(CompilationDto compilationDto);

    void deleteCompilation(long compId);

    CompilationDto updateCompilation(long compId, CompilationDto compilationDto);
}
