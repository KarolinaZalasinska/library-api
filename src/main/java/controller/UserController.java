package controller;

import dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.util.List;

@Api(value = "User Management System", tags = { "User" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @ApiOperation(value = "Create a new user")
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @ApiParam(value = "Provide user data to create a new user", required = true) @RequestBody UserDto userDto) {
        UserDto createdUser = service.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @GetMapping("/{id}")
    @ApiOperation(value = "Get a user by ID")
    public ResponseEntity<UserDto> getUserById(
            @ApiParam(value = "ID of the user", required = true) @PathVariable Long id) {
        UserDto userDto = service.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    @ApiOperation(value = "Get all users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a user by ID")
    public ResponseEntity<UserDto> updateUser(
            @ApiParam(value = "ID of the user", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated user data", required = true) @RequestBody UserDto userDto) {
        UserDto updatedUser = service.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(
            @ApiParam(value = "ID of the user", required = true) @PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
