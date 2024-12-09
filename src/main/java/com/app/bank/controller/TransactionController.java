package com.app.bank.controller;

import com.app.bank.entity.Transaction;
import com.app.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Transaction API's.")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Operation(
            summary = "Fetch the user account's transactions",
            description = "Returns the user account transactions"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account transaction fetched successfully"
    )
    @GetMapping("/transactions/{accountNumber}")
    public ResponseEntity<List<Transaction>> getLoggedInUserAccountInfo(@PathVariable String accountNumber){
        return transactionService.fetchTransactions(accountNumber);
    }
}
