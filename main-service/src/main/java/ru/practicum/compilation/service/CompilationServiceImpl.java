package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.participation.repository.ParticipationRepository;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.EntityNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.compilation.dto.CompilationMapper.toCompilation;
import static ru.practicum.compilation.dto.CompilationMapper.toCompilationDto;
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
    public CompilationDto createCompilation(CompilationDto compilationDto) {
        List<Event> eventList = eventRepository.findAllPublishedByEventIdList(compilationDto.getEvents());
        return toCompilationDto(compilationRepository.save(toCompilation(compilationDto, eventList)));
    }

    @Override
    public void deleteCompilation(long compId) {
        findCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(long compId, CompilationDto compilationDto) {
        Compilation compilation = findCompilation(compId);
        List<Event> eventList;
        if (compilationDto.getEvents() != null) {
            eventList = eventRepository.findAllPublishedByEventIdList(compilationDto.getEvents());
            compilation.setEvents(new HashSet<>(eventList));
        }
        if (compilationDto.getPinned() != null)
            compilation.setPinned(compilationDto.getPinned());
        if (StringUtils.isBlank(compilationDto.getTitle()))
            compilation.setTitle(compilationDto.getTitle());
        return toCompilationDto(compilation);
    }

    private Compilation findCompilation(long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Compilation with id=" + compId + "was not found"));
    }
}
