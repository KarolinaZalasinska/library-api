package com.example.libraryapi.controller;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.dto.ClientActivityDto;
import com.example.libraryapi.exceptions.copies.CopyNotAvailableException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.libraryapi.service.BorrowService;

import java.util.List;

@Api(value = "Borrow Management System", tags = {"Borrow"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/borrows")
public class BorrowController {
    private final BorrowService service;

    @ApiOperation(value = "Borrow a copy")
    @PostMapping("/borrow")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<Void> borrowCopy(
            @ApiParam(value = "Copy id", required = true) @RequestParam final Long copyId,
            @ApiParam(value = "User id", required = true) @RequestParam final Long userId) {
        service.borrowCopy(copyId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "Return a copy")
    @PostMapping("/return")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<Void> returnCopy(
            @ApiParam(value = "Copy id", required = true) @RequestParam final Long copyId,
            @ApiParam(value = "User id", required = true) @RequestParam final Long userId) {
        service.returnCopy(copyId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "Get borrow history for a user")
    @GetMapping("/history")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientActivityDto>> getBorrowHistoryForUser(
            @ApiParam(value = "User id", required = true) @RequestParam final Long userId) {
        List<ClientActivityDto> borrowHistory = service.getBorrowHistoryForUser(userId);
        return ResponseEntity.ok(borrowHistory);
    }

    @ApiOperation(value = "Check copy availability")
    @GetMapping("/availability")
    public ResponseEntity<Object> isCopyAvailable(
            @ApiParam(value = "Copy id", required = true) @RequestParam final Long copyId) {
        try {
            boolean copyAvailable = service.isCopyAvailable(copyId);
            return ResponseEntity.ok(copyAvailable);
        } catch (CopyNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Copy is not available");
        }
    }

    @ApiOperation(value = "Get current borrowed copies for client")
    @GetMapping()
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<CopyDto>> getCurrentBorrowedCopiesForClient(
            @ApiParam(value = "Client id", required = true) @RequestParam final Long clientId) {
        List<CopyDto> currentBorrowedCopiesForClient = service.getCurrentBorrowedCopiesForClient(clientId);
        return ResponseEntity.ok(currentBorrowedCopiesForClient);
    }
}
