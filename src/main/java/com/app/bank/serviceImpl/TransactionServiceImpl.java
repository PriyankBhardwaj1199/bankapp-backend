package com.app.bank.serviceImpl;

import com.app.bank.dto.TransactionDto;
import com.app.bank.entity.Transaction;
import com.app.bank.repository.TransactionRepository;
import com.app.bank.service.TransactionService;
import com.app.bank.utility.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
