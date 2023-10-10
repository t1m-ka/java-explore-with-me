package ru.practicum.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService service;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilation(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return null;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(
            @PathVariable long compId) {
        return null;
    }

    @PostMapping("/admin/compilations")
    public CompilationDto createCompilation(
            @RequestBody CompilationDto compilationDto) {
        return null;
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(
            @PathVariable long compId) {
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable long compId,
            @RequestBody CompilationDto compilationDto) {
        return null;
    }
}
