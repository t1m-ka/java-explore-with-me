package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;
import ru.practicum.util.exception.RequiredArgsMissingException;
import ru.practicum.util.exception.ValidationException;

import java.util.List;

import static ru.practicum.categories.dto.CategoryDtoValidator.validateNewCategory;
import static ru.practicum.util.VariableValidator.validatePageableParams;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        validateNewCategory(categoryDto);
        return service.createCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        if (catId == null)
            throw new RequiredArgsMissingException("Required variable is missing", "Path variable 'catId' is missing");
        if (catId <= 0)
            throw new ValidationException("Invalid path variable", "Path variable 'catId' should be positive");
        service.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(
            @PathVariable Long catId,
            @RequestBody CategoryDto categoryDto) {
        if (catId == null)
            throw new RequiredArgsMissingException("Required variable is missing", "Path variable 'catId' is missing");
        if (catId <= 0)
            throw new ValidationException("Invalid path variable", "Path variable 'catId' should be positive");
        validateNewCategory(categoryDto);
        return service.updateCategory(catId, categoryDto);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategoryList(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        validatePageableParams(from, size);
        if (from < 0)
            throw new ValidationException("Invalid page params", "Param 'from' should be positive or zero");
        if (size < 1)
            throw new ValidationException("Invalid page params", "Param 'size' should be positive");
        return service.getCategoryList(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        if (catId == null)
            throw new RequiredArgsMissingException("Required variable is missing", "Path variable 'catId' is missing");
        if (catId <= 0)
            throw new ValidationException("Invalid path variable", "Path variable 'catId' should be positive");
        return service.getCategoryById(catId);
    }
}
