package com.app.bank.service;

import com.app.bank.dto.TransactionDto;
import com.app.bank.entity.Transaction;
import com.app.bank.utility.BankResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionService {

    void saveTransaction(TransactionDto transactionDto);

    ResponseEntity<List<Transaction>> fetchTransactions(String accountNumber);

    BankResponse deleteTransaction(String transactionId);
}
