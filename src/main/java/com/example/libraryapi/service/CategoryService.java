package com.example.libraryapi.service;

import com.example.libraryapi.dto.CategoryDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.CategoryRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CategoryDto createCategory(@Valid CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    public CategoryDto getCategoryById(final Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.map(c -> modelMapper.map(c, CategoryDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Category with ID " + id + " was not found."));
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            return Collections.emptyList();
        } else {
            return categories.stream()
                    .map(c -> modelMapper.map(c, CategoryDto.class))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CategoryDto updateCategory(final Long id, @Valid CategoryDto categoryDto) {
        return categoryRepository.findById(id)
                .map(category -> {
                    modelMapper.map(categoryDto, category);
                    Category updatedCategory = categoryRepository.save(category);
                    return modelMapper.map(updatedCategory, CategoryDto.class);
                }).orElseThrow(() -> new ObjectNotFoundException("Category with ID " + id + " was not found."));
    }

    @Transactional
    public void deleteCategory(final Long id) {
        categoryRepository.deleteById(id);
    }


}
