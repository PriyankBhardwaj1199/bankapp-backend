package com.app.bank.service;

import com.app.bank.dto.BankStatementDto;
import com.app.bank.entity.BankStatement;
import com.app.bank.utility.BankResponse;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;

public interface BankStatementService {

    public BankResponse generateStatement(BankStatementDto bankStatementDto);

    ResponseEntity<List<BankStatement>> getAllBankStatement(String accountNumber);

    ResponseEntity<byte[]> downloadBankStatement(String accountNumber,Long id) throws SQLException;

    BankResponse deleteBankStatement(String accountNumber, Long id);
}
