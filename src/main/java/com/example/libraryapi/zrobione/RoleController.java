package com.example.libraryapi.zrobione;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Api(value = "Role Management System", tags = {"Role"})
public class RoleController {

    private final RoleService roleService;

    @ApiOperation(value = "Create a new role")
    @PostMapping
    public ResponseEntity<Role> createRole(
            @ApiParam(value = "Role to be created", required = true) @RequestBody UserRole role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @ApiOperation(value = "Get role by user role")
    @GetMapping("/{userRole}")
    public ResponseEntity<Role> getRoleByUserRole(
            @ApiParam(value = "User role to be searched", required = true) @PathVariable UserRole role) {
        Role userRole = roleService.findRoleByUserRole(role);
        return ResponseEntity.ok(userRole);
    }

    @ApiOperation(value = "Get all roles")
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @ApiOperation(value = "Delete role by user role")
    @DeleteMapping("/{userRole}")
    public ResponseEntity<Void> deleteRoleByUserRole(
            @ApiParam(value = "User role to be deleted", required = true) @PathVariable UserRole userRole) {
        roleService.deleteRoleByName(userRole);
        return ResponseEntity.noContent().build();
    }

}
