package com.app.bank.controller;

import com.app.bank.dto.BankStatementDto;
import com.app.bank.entity.Transaction;
import com.app.bank.service.BankStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bankStatement")
public class TransactionController {

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
    public List<Transaction> generateBankStatement(@RequestBody BankStatementDto bankStatementDto){
        return bankStatementService.generateStatement(bankStatementDto);
    }
}
