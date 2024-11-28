package com.app.bank.controller;

import com.app.bank.dto.BankStatementDto;
import com.app.bank.entity.Transaction;
import com.app.bank.service.BankStatementService;
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

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestBody BankStatementDto bankStatementDto){
        return bankStatementService.generateStatement(bankStatementDto);
    }
}
