package com.example.libraryapi.repository;

import com.example.libraryapi.model.ActionType;
import com.example.libraryapi.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findByUserIdAndActionType(Long id, ActionType actionType);

}
