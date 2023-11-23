package com.example.libraryapi.controller;

import com.example.libraryapi.dto.UserActivityDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.libraryapi.service.UserActivityService;

import java.util.List;

@Api(value = "User Activity Management System", tags = {"User Activity"})
@RestController
@RequestMapping("/user-activities")
@RequiredArgsConstructor
public class UserActivityController {
    public final UserActivityService service;

    @ApiOperation(value = "Show borrow history by user id")
    @GetMapping("/borrow-history/{userId}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<UserActivityDto>> getUserBorrowHistory(
            @ApiParam(value = "User id", required = true) @PathVariable final Long userId) {
        List<UserActivityDto> borrowHistory = service.getUserBorrowHistory(userId);
        return ResponseEntity.ok(borrowHistory);
    }

    @ApiOperation(value = "Show return history by user id")
    @GetMapping("/return-history/{userId}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<UserActivityDto>> getUserReturnHistory(
            @ApiParam(value = "User id", required = true) @PathVariable final Long userId) {
        List<UserActivityDto> returnHistory = service.getUserReturnHistory(userId);
        return ResponseEntity.ok(returnHistory);
    }
}
