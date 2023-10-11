package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.util.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.categories.dto.CategoryMapper.toCategory;
import static ru.practicum.categories.dto.CategoryMapper.toCategoryDto;
import static ru.practicum.util.PageParamsMaker.makePageable;

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

    @Override
    public List<CategoryDto> getCategoryList(int from, int size) {
        List<Category> categoryList = repository.findAll(makePageable(from, size)).getContent();
        return categoryList.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        Category category = repository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found", "Category with id=" + catId + " was not found"));
        return toCategoryDto(category);
    }
}
