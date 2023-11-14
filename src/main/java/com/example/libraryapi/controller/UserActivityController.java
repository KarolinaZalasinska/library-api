package com.example.libraryapi.controller;

import com.example.libraryapi.dto.UserActivityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.libraryapi.service.UserActivityService;

import java.util.List;

@RestController
@RequestMapping("/user-activity")
@RequiredArgsConstructor
public class UserActivityController {
    public final UserActivityService service;

    @GetMapping("/borrow-history/{userId}")
    public ResponseEntity<List<UserActivityDto>> getUserBorrowHistory(@PathVariable Long userId) {
        List<UserActivityDto> borrowHistory = service.getUserBorrowHistory(userId);
        return ResponseEntity.ok(borrowHistory);
    }

    @GetMapping("/return-history/{userId}")
    public ResponseEntity<List<UserActivityDto>> getUserReturnHistory(@PathVariable Long userId) {
        List<UserActivityDto> returnHistory = service.getUserReturnHistory(userId);
        return ResponseEntity.ok(returnHistory);
    }
}
