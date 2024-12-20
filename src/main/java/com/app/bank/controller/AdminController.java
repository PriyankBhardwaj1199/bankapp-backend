package com.app.bank.controller;

import com.app.bank.dto.CardsRequest;
import com.app.bank.dto.Statistics;
import com.app.bank.entity.Cards;
import com.app.bank.entity.User;
import com.app.bank.service.AdminService;
import com.app.bank.utility.BankResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin API's.")
@CrossOrigin(origins = "http://localhost:4200")
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
    @GetMapping("/fetchAllCards")
    public ResponseEntity<List<Cards>> getAllCardsInfo(){
        return adminService.fetchAllCards();
    }

    @Operation(
            summary = "Fetch all pending card approval",
            description = "Returns all pending card information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All pending card information fetched successfully"
    )
    @PostMapping("/card/action/{action}")
    public BankResponse actionOnCard(@RequestBody CardsRequest cardsRequest, @PathVariable String action){
        return adminService.actionOnCard(cardsRequest, action.toUpperCase());
    }

    @Operation(
            summary = "Fetch all statistical information",
            description = "Returns statistical information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All statistical information fetched successfully"
    )
    @GetMapping("/statistics")
    public Statistics getStatistics(){
        return adminService.fetchStatistics();
    }

}
