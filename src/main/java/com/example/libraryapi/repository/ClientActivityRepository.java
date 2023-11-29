package com.example.libraryapi.repository;

import com.example.libraryapi.model.ActionType;
import com.example.libraryapi.model.ClientActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ClientActivityRepository extends JpaRepository<ClientActivity, Long> {
    List<ClientActivity> findByClientIdAndActionType(Long id, ActionType actionType);

}
