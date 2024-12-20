package com.app.bank.repository;

import com.app.bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction,String> {

    int deleteByAccountNumber(String accountNumber);

    void deleteByTransactionId(String transactionId);

    List<Transaction> findAllByAccountNumberOrderByCreatedAtDesc(String accountNumber);

    Optional<Transaction> findByTransactionId(String transactionId);

}
