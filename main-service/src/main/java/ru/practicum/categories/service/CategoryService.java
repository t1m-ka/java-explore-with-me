package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);
}
