package web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.RegisterResponse;
import users.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
class UsersController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCommand command) {
        RegisterResponse response = userService.register(command.getUsername(), command.getPassword());
        if (response.isSuccess()) {
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

//    @PostMapping
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterCommand command){
//        return register
//                .register(command.username,command.password)
//                .handle(
//                        entity-> ResponseEntity.accepted().build(),
//                        error ->ResponseEntity.badRequest().body(error)
//                );
//    }


    @Data
    static class RegisterCommand {
        @Email
        private String username;
        @Size(min = 3, max = 30)
        private String password;
    }
}
