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

@Api(value = "Category Management System", tags = {"Category"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    @ApiOperation("Create new category")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<CategoryDto> createCategory(
            @ApiParam(value = "Provide category data to create a new category", required = true) @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = service.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get category by id")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoryDto> getCategoryById(
            @ApiParam(value = "Category id", required = true) @PathVariable final Long id) {
        CategoryDto categoryDto = service.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping
    @ApiOperation("Get all categories")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update a category by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<CategoryDto> updateCategory(
            @ApiParam(value = "Category id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Update category data", required = true) @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = service.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a category by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deleteCategory(
            @ApiParam(value = "Category id", required = true) @PathVariable final Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}