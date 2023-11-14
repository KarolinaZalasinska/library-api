package com.example.libraryapi.controller;

import com.example.libraryapi.dto.CopyDto;
import com.example.libraryapi.dto.UserActivityDto;
import com.example.libraryapi.exceptions.copies.CopyNotAvailableException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> borrowCopy(
            @ApiParam(value = "ID of the copy", required = true) @RequestParam final Long copyId,
            @ApiParam(value = "ID of the user", required = true) @RequestParam final Long userId) {
        service.borrowCopy(copyId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "Return a copy")
    @PostMapping("/return")
    public ResponseEntity<Void> returnCopy(
            @ApiParam(value = "ID of the copy", required = true) @RequestParam final Long copyId,
            @ApiParam(value = "ID of the user", required = true) @RequestParam final Long userId) {
        service.returnCopy(copyId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "Get borrow history for a user")
    @GetMapping("/history")
    public ResponseEntity<List<UserActivityDto>> getBorrowHistoryForUser(
            @ApiParam(value = "ID of the user", required = true) @RequestParam final Long userId) {
        List<UserActivityDto> borrowHistory = service.getBorrowHistoryForUser(userId);
        return ResponseEntity.ok(borrowHistory);
    }

    @ApiOperation(value = "Check copy availability")
    @GetMapping("/availability")
    public ResponseEntity<Object> isCopyAvailable(
            @ApiParam(value = "ID of the copy", required = true) @RequestParam final Long copyId) {
        try {
            boolean copyAvailable = service.isCopyAvailable(copyId);
            return ResponseEntity.ok(copyAvailable);
        } catch (CopyNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Copy is not available");
        }
    }

    @ApiOperation(value = "Get current borrowed copies for user")
    @GetMapping()
    public ResponseEntity<List<CopyDto>> getCurrentBorrowedCopiesForUser(
            @ApiParam(value = "ID of the user", required = true) @RequestParam final Long userId){
        List<CopyDto> currentBorrowedCopiesForUser = service.getCurrentBorrowedCopiesForUser(userId);
        return ResponseEntity.ok(currentBorrowedCopiesForUser);
    }
}
