package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;

import java.util.List;

import static ru.practicum.categories.dto.CategoryValidator.validateNewCategory;

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
    public void deleteCategory(@PathVariable long catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(
            @PathVariable long catId,
            @RequestBody CategoryDto categoryDto) {
        validateNewCategory(categoryDto);
        return service.updateCategory(catId, categoryDto);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategoryList(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return null;
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        return null;
    }
}
