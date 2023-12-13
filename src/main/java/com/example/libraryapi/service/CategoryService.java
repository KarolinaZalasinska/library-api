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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CategoryDto createCategory(@Valid CategoryDto categoryDto) {
        Category newCategory = categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(newCategory, CategoryDto.class);
    }

    public CategoryDto getCategoryById(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .orElseThrow(() -> new ObjectNotFoundException("Category with id " + id + " was not found."));
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto updateCategory(final Long id, @Valid CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id " + id + " was not found."));

        modelMapper.map(categoryDto, category);
        Category updatedCategory = categoryRepository.save(category);

        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Transactional
    public void deleteCategory(final Long id) {
        categoryRepository.deleteById(id);
    }


}
