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

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
@Api(tags = "Publisher Management System", value = "{Publisher}")
public class PublisherController {
    private final PublisherService publisherService;

    @ApiOperation(value = "Create a new publisher")
    @PostMapping
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<PublisherDto> createPublisher(
            @ApiParam(value = "Publisher details", required = true) @Valid @RequestBody PublisherDto publisherDto) {
        PublisherDto createdPublisher = publisherService.createPublisher(publisherDto);
        return ResponseEntity.ok(createdPublisher);
    }

    @ApiOperation(value = "Get publisher by id")
    @GetMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<PublisherDto> getPublisherById(
            @ApiParam(value = "Publisher ID", required = true) @PathVariable final Long id) {
        PublisherDto publisherDto = publisherService.getPublisherById(id);
        return ResponseEntity.ok(publisherDto);
    }

    @ApiOperation(value = "Get all publishers")
    @GetMapping
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<PublisherDto>> getAllPublishers() {
        List<PublisherDto> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok(publishers);
    }

    @ApiOperation(value = "Update publisher by id")
    @PutMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<PublisherDto> updatePublisher(
            @ApiParam(value = "Publisher id", required = true) @PathVariable final Long id,
            @ApiParam(value = "Updated publisher data", required = true) @Valid @RequestBody PublisherDto publisherDto) {
        PublisherDto updatedPublisher = publisherService.updatePublisher(id, publisherDto);
        return ResponseEntity.ok(updatedPublisher);
    }

    @ApiOperation(value = "Delete publisher by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<Void> deletePublisher(
            @ApiParam(value = "Publisher id", required = true) @PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
    @ApiOperation(value = "Get books by publisher id")
    @GetMapping("/{id}/books")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<BookDto>> getBooksByPublisher(
            @ApiParam(value = "Publisher id", required = true) @PathVariable Long id) {
        List<BookDto> books = publisherService.getBooksByPublisher(id);
        return ResponseEntity.ok(books);
    }
}
