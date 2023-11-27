package com.example.libraryapi.model;

import domain.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

}

