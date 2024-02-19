package com.example.libraryapi.usersRoles;

import com.example.libraryapi.exceptions.RoleNotFoundException;
import com.example.libraryapi.usersRoles.UserRole;
import com.example.libraryapi.usersRoles.Role;
import com.example.libraryapi.usersRoles.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    // dynamiczne tworzenie ról na podstawie roleName
    public Role createRole(UserRole role) {
        Role userRole = new Role();
        userRole.setRole(role);
        return roleRepository.save(userRole);
    }

    public Role findRoleByUserRole(UserRole role) {
        return roleRepository.findByRole(role)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + role.name()));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void deleteRoleByName(UserRole role) {
        roleRepository.deleteByRole(role);
    }

}
