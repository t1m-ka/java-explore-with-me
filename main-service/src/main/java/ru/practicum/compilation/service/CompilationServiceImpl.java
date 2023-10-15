package ru.practicum.compilation.service;

import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

@Service
public class CompilationServiceImpl implements CompilationService {
    @Override
    public List<CompilationDto> getCompilation(Boolean pinned, int from, int size) {
        return null;
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        return null;
    }

    @Override
    public CompilationDto createCompilation(CompilationDto compilationDto) {
        return null;
    }

    @Override
    public void deleteCompilation(long compId) {

    }

    @Override
    public CompilationDto updateCompilation(long compId, CompilationDto compilationDto) {
        return null;
    }
}
