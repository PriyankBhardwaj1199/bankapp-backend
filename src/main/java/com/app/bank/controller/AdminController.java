package com.app.bank.controller;

import com.app.bank.entity.Cards;
import com.app.bank.entity.User;
import com.app.bank.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin API's.")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(
            summary = "Fetch all user account's information",
            description = "Returns all user accounts information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All Accounts information fetched successfully"
    )
    @GetMapping("/fetchAllAccount")
    public ResponseEntity<List<User>> getAllUserAccountsInfo(){
        return adminService.fetchAllUserAccounts();
    }

    @Operation(
            summary = "Fetch all pending card approval",
            description = "Returns all pending card information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All pending card information fetched successfully"
    )
    @GetMapping("/fetchCards")
    public ResponseEntity<List<Cards>> getAllPendingCardsInfo(){
        return adminService.fetchAllPendingCards();
    }
}
