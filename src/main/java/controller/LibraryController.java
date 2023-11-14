package controller;

import dto.BookDto;
import dto.LibraryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.LibraryService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(value = "Library Management System", tags = {"Library"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService service;

    @ApiOperation(value = "Create a new library")
    @PostMapping()
    public ResponseEntity<LibraryDto> createLibrary(
            @ApiParam(value = "Provide library data to create a new library", required = true) @Valid @RequestBody LibraryDto libraryDto) {
        LibraryDto createdLibrary = service.createLibrary(libraryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLibrary);
    }

    @ApiOperation(value = "Get a library by ID")
    @GetMapping("/{id}")
    public ResponseEntity<LibraryDto> getLibraryById(
            @ApiParam(value = "ID of the library", required = true) @PathVariable final Long id) {
        LibraryDto libraryDto = service.getLibraryById(id);
        return ResponseEntity.ok(libraryDto);
    }

    @ApiOperation(value = "Get all libraries")
    @GetMapping()
    public ResponseEntity<List<LibraryDto>> getAllLibraries() {
        List<LibraryDto> libraries = service.getAllLibraries();
        return ResponseEntity.ok(libraries);
    }

    @ApiOperation(value = "Update a library by ID")
    @PutMapping("/{id}")
    public ResponseEntity<LibraryDto> updateLibrary(
            @ApiParam(value = "ID of the library", required = true) @Valid @PathVariable final Long id,
            @ApiParam(value = "Update library data", required = true) @RequestBody LibraryDto libraryDto) {
        LibraryDto updatedLibrary = service.updateLibrary(id, libraryDto);
        return ResponseEntity.ok(updatedLibrary);
    }

    @ApiOperation(value = "Update specific fields of a library by ID")
    @PatchMapping("/{id}")
    public ResponseEntity<LibraryDto> updateLibraryField(
            @ApiParam(value = "Library ID", required = true) @Valid @PathVariable final Long id,
            @ApiParam(value = "Fields to update", required = true) @RequestBody Map<String, String> fieldsToUpdate) {
        LibraryDto updatedLibrary = service.updateLibraryField(id, fieldsToUpdate);
        return ResponseEntity.ok(updatedLibrary);
    }

    @ApiOperation(value = "Delete a library by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(
            @ApiParam(value = "Library ID", required = true) @PathVariable final Long id) {
        service.deleteLibrary(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Get books in a library by ID")
    @GetMapping("/{id}/books")
    public ResponseEntity<Set<BookDto>> getBooksInLibrary(
            @ApiParam(value = "Library ID", required = true) @PathVariable final Long id) {
        Set<BookDto> books = service.getBooksInLibrary(id);
        return ResponseEntity.ok(books);
    }

    @ApiOperation(value = "Add a book to a library by ID")
    @PostMapping("/{libraryId}/add-book/{bookId}")
    public ResponseEntity<LibraryDto> addBookToLibrary(
            @ApiParam(value = "Library ID", required = true) @PathVariable final Long libraryId,
            @ApiParam(value = "Book ID", required = true) @PathVariable final Long bookId) {
        LibraryDto updatedLibrary = service.addBookToLibrary(libraryId, bookId);
        return ResponseEntity.ok(updatedLibrary);
    }
}
