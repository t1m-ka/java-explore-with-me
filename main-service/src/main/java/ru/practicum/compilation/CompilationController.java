package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

import static ru.practicum.compilation.dto.CompilationValidator.validateNewCompilationDto;
import static ru.practicum.compilation.dto.CompilationValidator.validateUpdateCompilationDto;
import static ru.practicum.util.VariableValidator.*;

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
        validatePageableParams(from, size);
        return service.getCompilation(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(
            @PathVariable Long compId) {
        validateNotNullObject(compId, "compId");
        validatePositiveNumber(compId, "compId");
        return service.getCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(
            @RequestBody NewCompilationDto newCompilationDto) {
        validateNotNullObject(newCompilationDto, "CompilationDto");
        validateNewCompilationDto(newCompilationDto);
        return service.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(
            @PathVariable Long compId) {
        validateNotNullObject(compId, "compId");
        validatePositiveNumber(compId, "compId");
        service.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable Long compId,
            @RequestBody UpdateCompilationDto updateCompilationDto) {
        validateNotNullObject(compId, "compId");
        validatePositiveNumber(compId, "compId");
        validateNotNullObject(updateCompilationDto, "CompilationDto");
        validateUpdateCompilationDto(updateCompilationDto);
        return service.updateCompilation(compId, updateCompilationDto);
    }
}
