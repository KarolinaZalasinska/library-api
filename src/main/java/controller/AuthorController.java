package controller;

import dto.AuthorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AuthorService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService service;

    @PostMapping
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto author = service.createAuthor(authorDto);
        return author != null
                ? ResponseEntity.status(HttpStatus.CREATED).body(author)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create author.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable Long id, @RequestBody AuthorDto updatedAuthor) {
        AuthorDto updated = service.updateAuthor(id, updatedAuthor);

        if (updated != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update author.");
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id, @RequestBody AuthorDto updatedAuthor) {
//        AuthorDto existingAuthor = authorService.getAuthorById(id);
//        return existingAuthor != null
//                ? ResponseEntity.ok(authorService.updateAuthor(id, updatedAuthor))
//                : ResponseEntity.notFound().build();
//    }


    @GetMapping("/{id}")

    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        AuthorDto authorDto = service.getAuthorById(id);
        return authorDto != null
                ? ResponseEntity.ok(authorDto)
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> authors = service.getAllAuthors();

        if (authors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authors);
        } else {
            return ResponseEntity.ok(authors);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        service.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
