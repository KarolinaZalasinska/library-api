package com.example.libraryapi.controller;

import com.example.libraryapi.dto.CategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.CategoryService;

import java.util.List;

@Api(value = "Category Management System", tags = {"Category"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;

    @ApiOperation("Create a new category")
    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(
            @ApiParam(value = "Provide category data to create a new category", required = true) @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = service.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @ApiOperation("Get a category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @ApiParam(value = "ID of the category", required = true) @PathVariable final Long id) {
        CategoryDto categoryDto = service.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @ApiOperation("Get all categories")
    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @ApiOperation("Update a category by ID")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @ApiParam(value = "ID of the category", required = true) @PathVariable final Long id,
            @ApiParam(value = "Update category data", required = true) @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = service.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @ApiOperation("Delete a category by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @ApiParam(value = "ID of the category", required = true) @PathVariable final Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}