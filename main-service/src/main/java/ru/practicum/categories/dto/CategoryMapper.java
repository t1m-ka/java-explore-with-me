package ru.practicum.categories.dto;

import ru.practicum.categories.model.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName()
        );
    }
}
