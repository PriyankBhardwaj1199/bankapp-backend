package com.app.bank.service;

import com.app.bank.dto.BankStatementDto;
import com.app.bank.entity.BankStatement;
import com.app.bank.utility.BankResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BankStatementService {

    public BankResponse generateStatement(BankStatementDto bankStatementDto);

    ResponseEntity<List<BankStatement>> getAllBankStatement(String accountNumber);
}
