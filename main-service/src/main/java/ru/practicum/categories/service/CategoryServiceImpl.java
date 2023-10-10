package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exception.EntityNotFoundException;

import static ru.practicum.categories.dto.CategoryMapper.toCategory;
import static ru.practicum.categories.dto.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return toCategoryDto(repository.save(toCategory(categoryDto)));
    }

    @Override
    public void deleteCategory(long catId) {
        repository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found", "Category with id=" + catId + " was not found"));
        repository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        repository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found", "Category with id=" + catId + " was not found"));
        return toCategoryDto(repository.save(toCategory(categoryDto)));
    }
}
