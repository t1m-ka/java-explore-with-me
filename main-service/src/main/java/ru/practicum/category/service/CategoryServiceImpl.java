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

import static ru.practicum.category.dto.CategoryDtoValidator.validateNewCategory;
import static ru.practicum.category.dto.CategoryMapper.toCategory;
import static ru.practicum.category.dto.CategoryMapper.toCategoryDto;
import static ru.practicum.util.PageParamsMaker.makePageable;
import static ru.practicum.util.VariableValidator.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        validateNewCategory(categoryDto);
        return toCategoryDto(repository.save(toCategory(categoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        validateNotNullObject(catId, "catId");
        validatePositiveNumber(catId, "catId");
        findCategory(catId);
        repository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        validateNotNullObject(catId, "catId");
        validatePositiveNumber(catId, "catId");
        validateNewCategory(categoryDto);
        Category category = findCategory(catId);
        if (category.getName().equals(categoryDto.getName()))
            return toCategoryDto(category);
        return toCategoryDto(repository.save(toCategory(categoryDto)));
    }

    @Override
    public List<CategoryDto> getCategoryList(int from, int size) {
        validatePageableParams(from, size);
        List<Category> categoryList = repository.findAll(makePageable(from, size)).getContent();
        return categoryList.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        validateNotNullObject(catId, "catId");
        validatePositiveNumber(catId, "catId");
        Category category = findCategory(catId);
        return toCategoryDto(category);
    }

    private Category findCategory(long catId) {
        return repository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "Category with id=" + catId + " was not found"));
    }
}
