package com.example.libraryapi.controller;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.dto.ClientActivityDto;
import com.example.libraryapi.exceptions.copies.CopyNotAvailableException;
import com.example.libraryapi.model.Copy;
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

    @PostMapping("/borrow")
    @ApiOperation(value = "Borrow copy")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> borrowCopy(
            @ApiParam(value = "Copy id", required = true) @RequestParam final Long copyId,
            @ApiParam(value = "Client id", required = true) @RequestParam final Long clientId) {
        service.borrowCopy(copyId, clientId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/return")
    @ApiOperation(value = "Return copy")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<Void> returnCopy(
            @ApiParam(value = "Copy id", required = true) @RequestParam final Long copyId,
            @ApiParam(value = "Client id", required = true) @RequestParam final Long clientId) {
        service.returnCopy(copyId, clientId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/client-history")
    @ApiOperation(value = "Get borrow history for client")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientActivityDto>> getBorrowHistoryForUser(
            @ApiParam(value = "Client id", required = true) @RequestParam final Long userId) {
        List<ClientActivityDto> borrowHistory = service.getBorrowHistoryForUser(userId);
        return ResponseEntity.ok(borrowHistory);
    }

    @GetMapping("/availability")
    @ApiOperation(value = "Check copy availability")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Object> isCopyAvailable(
            @ApiParam(value = "Copy id", required = true) @RequestParam final Long copyId) {
        try {
            Copy copy = service.findCopyById(copyId);
            boolean copyAvailable = service.isCopyAvailable(copy);
            return ResponseEntity.ok(copyAvailable);
        } catch (CopyNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Copy is not available");
        }
    }


    @GetMapping
    @ApiOperation(value = "Get current borrowed copies for client")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.users.UserRole).ADMIN.name())")
    public ResponseEntity<List<CopyDto>> getCurrentBorrowedCopiesForClient(
            @ApiParam(value = "Client id", required = true) @RequestParam final Long clientId) {
        List<CopyDto> currentBorrowedCopiesForClient = service.getCurrentBorrowedCopiesForClient(clientId);
        return ResponseEntity.ok(currentBorrowedCopiesForClient);
    }
}
