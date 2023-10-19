package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.util.exception.EntityNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.compilation.dto.CompilationMapper.*;
import static ru.practicum.util.PageParamsMaker.makePageable;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilation(Boolean pinned, int from, int size) {
        return compilationRepository.findAllCompilations(pinned, makePageable(from, size)).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        return toCompilationDto(findCompilation(compId));
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> eventList = eventRepository.findAllByEventIdList(newCompilationDto.getEvents());
        return toCompilationDto(compilationRepository.save(toCompilation(newCompilationDto, eventList)));
    }

    @Override
    public void deleteCompilation(long compId) {
        findCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(long compId, UpdateCompilationDto updateCompilationDto) {
        Compilation compilation = findCompilation(compId);
        List<Event> eventList;
        if (updateCompilationDto.getEvents() != null) {
            eventList = eventRepository.findAllPublishedByEventIdList(updateCompilationDto.getEvents());
            compilation.setEvents(new HashSet<>(eventList));
        }
        if (updateCompilationDto.getPinned() != null)
            compilation.setPinned(updateCompilationDto.getPinned());
        if (!StringUtils.isBlank(updateCompilationDto.getTitle()))
            compilation.setTitle(updateCompilationDto.getTitle());
        return toCompilationDto(compilation);
    }

    private Compilation findCompilation(long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Compilation with id=" + compId + "was not found"));
    }
}
