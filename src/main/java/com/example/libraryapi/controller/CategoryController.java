package com.example.libraryapi.controller;

import com.example.libraryapi.dto.CategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.CategoryService;

import java.util.List;
import java.util.Map;

@Api(value = "Category Management System", tags = {"Category"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    @ApiOperation(value = "Create new category", notes = "Creates a new category based on the provided CategoryDto.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<CategoryDto> createCategory(
            @ApiParam(value = "Provide category data to create a new category", required = true) @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = service.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get category by id", notes = "Retrieves a category based on the provided category id.")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoryDto> getCategoryById(
            @ApiParam(value = "Category id", required = true) @PathVariable final Long id) {
        CategoryDto categoryDto = service.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all categories", notes = "Retrieves information about all categories in the system.")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update category by id", notes = "Updates an existing category based on the provided fields.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<CategoryDto> updateCategory(
            @ApiParam(value = "Category id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Update category data", required = true) @RequestBody Map<String, String> fieldsToUpdate) {
        service.updateCategory(id, fieldsToUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a category by id", notes = "Deletes a category based on the provided category id.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteCategory(
            @ApiParam(value = "Category id", required = true) @PathVariable final Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{categoryId}/add-book/{bookId}")
    @ApiOperation(value = "Add book to category", notes = "Adds a book to the specified category.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<CategoryDto> addBookToCategory(
            @ApiParam(value = "Category id", required = true) @PathVariable final Long categoryId,
            @ApiParam(value = "Book id", required = true) @PathVariable final Long bookId) {
        CategoryDto updatedCategory = service.addBookToCategory(categoryId, bookId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

}