package com.app.bank.controller;

import com.app.bank.dto.BankStatementDto;
import com.app.bank.entity.BankStatement;
import com.app.bank.service.BankStatementService;
import com.app.bank.utility.BankResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@CrossOrigin(origins = "http://localhost:4200")
public class BankStatementController {

    @Autowired
    private BankStatementService bankStatementService;

    @Operation(
            summary = "Generate bank statement for the user's account",
            description = "Generates the bank statement and sends it via email to the user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Statement pdf sent via email successfully"
    )
    @GetMapping
    public BankResponse generateBankStatement(@RequestBody BankStatementDto bankStatementDto){
        return bankStatementService.generateStatement(bankStatementDto);
    }

    @Operation(
            summary = "Get all bank statements for the user's account",
            description = "Get the bank statements"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Statements fetched successfully"
    )
    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<BankStatement>> getAllBankStatement(@PathVariable String accountNumber){
        return bankStatementService.getAllBankStatement(accountNumber);
    }

    @Operation(
            summary = "Download bank statement for the user's account",
            description = "Downloads the bank statement"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Statements downloaded successfully"
    )
    @GetMapping("/download/{accountNumber}/{id}")
    public ResponseEntity<byte[]> downloadBankStatement(@PathVariable String accountNumber,@PathVariable Long id) throws SQLException {
        return bankStatementService.downloadBankStatement(accountNumber,id);
    }
}
