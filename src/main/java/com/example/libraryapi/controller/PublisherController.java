package com.example.libraryapi.controller;

import com.example.libraryapi.dto.BookDto;
import com.example.libraryapi.dto.PublisherDto;
import com.example.libraryapi.service.PublisherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
@Api(tags = "Publisher Management System", value = "{Publisher}")
public class PublisherController {
    private final PublisherService publisherService;

    @PostMapping
    @ApiOperation(value = "Create new publisher")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<PublisherDto> createPublisher(
            @ApiParam(value = "Publisher details", required = true) @Valid @RequestBody PublisherDto publisherDto) {
        PublisherDto createdPublisher = publisherService.createPublisher(publisherDto);
        return ResponseEntity.ok(createdPublisher);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get publisher by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<PublisherDto> getPublisherById(
            @ApiParam(value = "Publisher id", required = true) @PathVariable final Long id) {
        PublisherDto publisherDto = publisherService.getPublisherById(id);
        return ResponseEntity.ok(publisherDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all publishers")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<PublisherDto>> getAllPublishers() {
        List<PublisherDto> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok(publishers);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update specified fields for a publisher with the given publisher id",
            notes = "Updates an existing publisher based on the provided fields.")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<PublisherDto> updatePublisherFields(
            @ApiParam(value = "Publisher id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Fields to update", required = true) @Valid @RequestBody Map<String, String> fieldsToUpdate) {
        PublisherDto updatedPublisher = publisherService.updatePublisher(id, fieldsToUpdate);
        return ResponseEntity.ok(updatedPublisher);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete publisher by id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deletePublisher(
            @ApiParam(value = "Publisher id", required = true) @PathVariable final Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/books")
    @ApiOperation(value = "Get books by publisher id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<BookDto>> getBooksByPublisher(
            @ApiParam(value = "Publisher id", required = true) @PathVariable final Long id) {
        List<BookDto> books = publisherService.getBooksByPublisher(id);
        return ResponseEntity.ok(books);
    }
}
