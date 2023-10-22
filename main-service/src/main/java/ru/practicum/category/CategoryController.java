package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return service.createCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(
            @PathVariable Long catId,
            @RequestBody CategoryDto categoryDto) {
        return service.updateCategory(catId, categoryDto);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategoryList(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return service.getCategoryList(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        return service.getCategoryById(catId);
    }
}
