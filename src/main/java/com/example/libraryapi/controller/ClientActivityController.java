package com.example.libraryapi.controller;

import com.example.libraryapi.dto.ClientActivityDto;
import com.example.libraryapi.model.ActionType;
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
@RequiredArgsConstructor
@RequestMapping("/client-activities")
public class ClientActivityController {

    private final ClientActivityService clientActivityService;

    @GetMapping("/history/{clientId}/borrow")
    @ApiOperation(value = "Show borrow history by client id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientActivityDto>> getClientBorrowHistory(
            @ApiParam(value = "Client id", required = true) @PathVariable final Long clientId) {
        List<ClientActivityDto> borrowHistory = clientActivityService.getClientActivityHistory(clientId, ActionType.BORROW);
        return ResponseEntity.ok(borrowHistory);
    }

    @GetMapping("/history/{clientId}/return")
    @ApiOperation(value = "Show return history by client id")
    @PreAuthorize("isFullyAuthenticated() and hasAuthority(T(com.example.libraryapi.usersRoles.UserRole).ADMIN.name())")
    public ResponseEntity<List<ClientActivityDto>> getClientReturnHistory(
            @ApiParam(value = "Client id", required = true) @PathVariable final Long clientId) {
        List<ClientActivityDto> returnHistory = clientActivityService.getClientActivityHistory(clientId, ActionType.RETURN);
        return ResponseEntity.ok(returnHistory);
    }
}
