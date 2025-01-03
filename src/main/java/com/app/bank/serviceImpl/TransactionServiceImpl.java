package com.app.bank.serviceImpl;

import com.app.bank.dto.TransactionDto;
import com.app.bank.entity.Transaction;
import com.app.bank.repository.TransactionRepository;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.TransactionService;
import com.app.bank.utility.BankResponse;
import com.app.bank.utility.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {

        Optional<BigDecimal> currentBalance =
                userRepository.findAccountBalanceByAccountNumber(transactionDto.getAccountNumber());

        BigDecimal balance = BigDecimal.ZERO;
        if (currentBalance.isPresent()) {
                balance = currentBalance.get();
        }

        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .runningBalance(balance)
                .status(TransactionResponse.SUCCESS.getMessage())
                .build();

        transactionRepository.save(transaction);
    }

    @Override
    public ResponseEntity<List<Transaction>> fetchTransactions(String accountNumber) {
        return ResponseEntity.ok(transactionRepository.findAllByAccountNumberOrderByCreatedAtDesc(accountNumber));
    }

    @Override
    public BankResponse deleteTransaction(String transactionId) {
        // check if transaction exists

        Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionId);

        if(transaction.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(TransactionResponse.TRANSACTION_NOT_FOUND.getCode())
                    .responseMessage(TransactionResponse.TRANSACTION_NOT_FOUND.getMessage())
                    .build();
        }

        Transaction deleteTransaction = transaction.get();

        transactionRepository.deleteByTransactionId(deleteTransaction.getTransactionId());

        return BankResponse.builder()
                .responseCode(TransactionResponse.DELETED.getCode())
                .responseMessage(TransactionResponse.DELETED.getMessage())
                .build();
    }
}

