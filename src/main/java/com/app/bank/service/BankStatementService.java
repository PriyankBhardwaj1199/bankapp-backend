package com.app.bank.service;

import com.app.bank.dto.BankStatementDto;
import com.app.bank.entity.Transaction;

import java.util.List;

public interface BankStatementService {

    public List<Transaction> generateStatement(BankStatementDto bankStatementDto);
}
