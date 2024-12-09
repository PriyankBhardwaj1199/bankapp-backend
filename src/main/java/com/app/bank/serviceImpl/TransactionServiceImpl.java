package com.app.bank.serviceImpl;

import com.app.bank.dto.TransactionDto;
import com.app.bank.entity.Transaction;
import com.app.bank.repository.TransactionRepository;
import com.app.bank.service.TransactionService;
import com.app.bank.utility.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status(TransactionResponse.SUCCESS.getMessage())
                .build();

        transactionRepository.save(transaction);
    }

    @Override
    public ResponseEntity<List<Transaction>> fetchTransactions(String accountNumber) {
        return ResponseEntity.ok(transactionRepository.findAllByAccountNumber(accountNumber));
    }
}
