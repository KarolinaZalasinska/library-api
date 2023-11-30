package com.example.libraryapi.zrobione;

import com.example.libraryapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String user);
}
