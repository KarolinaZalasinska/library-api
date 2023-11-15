package com.example.libraryapi.repository;

import com.example.libraryapi.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findByUserIdAndActionBorrow(Long id, String actionBorrow);
    List<UserActivity> findByUserIdAndActionReturn(Long id, String actionReturn);

}
