package com.example.libraryapi.service;

import com.example.libraryapi.dto.CategoryDto;
import com.example.libraryapi.exceptions.ObjectNotFoundException;
import com.example.libraryapi.model.Book;
import lombok.RequiredArgsConstructor;
import com.example.libraryapi.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libraryapi.repository.CategoryRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Service class for managing categories.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    /**
     * Creates a new category based on the provided CategoryDto.
     *
     * @param categoryDto The CategoryDto containing information for the new category.
     * @return The created CategoryDto.
     */
    @Transactional
    public CategoryDto createCategory(@Valid CategoryDto categoryDto) {
        Category newCategory = categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(newCategory, CategoryDto.class);
    }

    /**
     * Retrieves category information by categoryId.
     *
     * @param id The identifier for the category.
     * @return The CategoryDto associated with the given categoryId.
     * @throws ObjectNotFoundException if the category is not found.
     */
    public CategoryDto getCategoryById(final Long id) {
        Category category = getCategoryOrThrow(id);
        return modelMapper.map(category, CategoryDto.class);
    }

    /**
     * Retrieves a list of all categories.
     *
     * @return A List of CategoryDto representing all categories.
     */
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing category based on the provided fields.
     *
     * @param id             The identifier for the category to be updated.
     * @param fieldsToUpdate A Map containing fields to be updated along with their new values.
     * @throws ObjectNotFoundException  if the category is not found.
     * @throws IllegalArgumentException if invalid or unsupported fields are specified,
     *                                  or if the field values fail validation.
     */
    public void updateCategory(final Long id, Map<String, String> fieldsToUpdate) {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        Category category = getCategoryOrThrow(id);

        Map<String, BiConsumer<Category, String>> fieldSetters = Map.of(
                "name", this::updateName
        );

        for (Map.Entry<String, String> entry : fieldsToUpdate.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            fieldSetters.getOrDefault(field, (cat, val) -> {
                throw new IllegalArgumentException("Invalid field specified: " + field);
            }).accept(category, value);
        }
    }

    private void updateName(Category category, String name) {
        if (isValidName(name)) {
            category.setName(name);
        } else {
            throw new IllegalArgumentException("Invalid value for name: " + name);
        }
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    @Transactional
    public void deleteCategory(final Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Adds a book to the specified category.
     *
     * @param categoryId The identifier of the category.
     * @param bookId     The identifier of the book to be added.
     * @return The updated CategoryDto.
     * @throws ObjectNotFoundException  if the category or book is not found.
     * @throws IllegalArgumentException if the book is already in the category.
     */
    @Transactional
    public CategoryDto addBookToCategory(Long categoryId, Long bookId) {
        Category category = getCategoryOrThrow(categoryId);
        Book book = bookService.getBookOrThrow(bookId);

        if (category.getBooks().contains(book)) {
            throw new IllegalArgumentException("Book is already in the category.");
        }

        category.getBooks().add(book);
        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDto.class);
    }

    Category getCategoryOrThrow(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id " + categoryId + " was not found."));
    }

}
