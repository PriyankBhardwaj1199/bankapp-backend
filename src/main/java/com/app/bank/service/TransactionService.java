package com.app.bank.service;

import com.app.bank.dto.TransactionDto;

public interface TransactionService {

    void saveTransaction(TransactionDto transactionDto);
}
