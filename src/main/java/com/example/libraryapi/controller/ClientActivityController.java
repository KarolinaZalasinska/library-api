package com.example.libraryapi.controller;

import com.example.libraryapi.dto.ClientActivityDto;
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
import com.example.libraryapi.service.ClientActivityService;

import java.util.List;

@Api(value = "Client Activity Management System", tags = {"Client Activity"})
@RestController
@RequestMapping("/client-activities")
@RequiredArgsConstructor
public class ClientActivityController {
    public final ClientActivityService service;

    @ApiOperation(value = "Show borrow history by client id")
    @GetMapping("/borrow-history/{clientId}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientActivityDto>> getClientBorrowHistory(
            @ApiParam(value = "Client id", required = true) @PathVariable final Long clientId) {
        List<ClientActivityDto> borrowHistory = service.getClientBorrowHistory(clientId);
        return ResponseEntity.ok(borrowHistory);
    }

    @ApiOperation(value = "Show return history by client id")
    @GetMapping("/return-history/{clientId}")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.model.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientActivityDto>> getClientReturnHistory(
            @ApiParam(value = "Client id", required = true) @PathVariable final Long clientId) {
        List<ClientActivityDto> returnHistory = service.getClientReturnHistory(clientId);
        return ResponseEntity.ok(returnHistory);
    }
}
