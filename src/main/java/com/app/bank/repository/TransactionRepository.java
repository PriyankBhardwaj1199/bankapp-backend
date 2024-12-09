package com.app.bank.repository;

import com.app.bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction,String> {

    int deleteByAccountNumber(String accountNumber);

    List<Transaction> findAllByAccountNumber(String accountNumber);
}
