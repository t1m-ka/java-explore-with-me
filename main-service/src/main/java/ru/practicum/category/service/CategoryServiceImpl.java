package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.util.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.category.dto.CategoryMapper.toCategory;
import static ru.practicum.category.dto.CategoryMapper.toCategoryDto;
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
                () -> new EntityNotFoundException("Required entity not found",
                        "Category with id=" + catId + " was not found"));
        repository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        Category category = repository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Category with id=" + catId + " was not found"));
        if (category.getName().equals(categoryDto.getName()))
            return toCategoryDto(category);
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
                () -> new EntityNotFoundException("Required entity not found",
                        "Category with id=" + catId + " was not found"));
        return toCategoryDto(category);
    }
}
