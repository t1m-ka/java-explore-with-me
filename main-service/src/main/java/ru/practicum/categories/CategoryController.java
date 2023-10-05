package ru.practicum.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return null;
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {

    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(
            @PathVariable long catId,
            @RequestBody CategoryDto categoryDto) {
        return null;
    }
}
