package web;

import com.example.libraryapi.service.LibraryUserService;
import domain.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final LibraryUserService libraryUserService;

    public RegistrationController(LibraryUserService libraryUserService) {
        this.libraryUserService = libraryUserService;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        libraryUserService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
