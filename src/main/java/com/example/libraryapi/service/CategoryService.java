package com.example.libraryapi.service;

import com.example.libraryapi.dto.CategoryDto;
import com.example.libraryapi.exceptions.ObjectNotFoundInRepositoryException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.mapper.CategoryMapper;
import com.example.libraryapi.model.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.CategoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    public final CategoryRepository repository;
    public final CategoryMapper mapper;

    @Transactional
    public CategoryDto createCategory(final CategoryDto categoryDto) {
        Category category = mapper.toEntity(categoryDto);
        Category savedCategory = repository.save(category);
        return mapper.toDto(savedCategory);
    }

    public CategoryDto getCategoryById(final Long id) {
        Optional<Category> optionalCategory = repository.findById(id);
        return optionalCategory.map(mapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("The category with the given ID could not be found.", id));
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = repository.findAll();

        if (categories.isEmpty()) {
            return Collections.emptyList();
        } else {
            return categories.stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CategoryDto updateCategory(final Long id, CategoryDto categoryDto) {
        return repository.findById(id)
                .map(category -> {
                    mapper.updateEntityFromDto(categoryDto, category);
                    Category updateCategory = repository.save(category);
                    return mapper.toDto(updateCategory);
                }).orElseThrow(() -> new ObjectNotFoundInRepositoryException("The category with the given ID could not be updated.", id));
    }

    @Transactional
    public void deleteCategory(final Long id) {
        repository.deleteById(id);
    }


}
