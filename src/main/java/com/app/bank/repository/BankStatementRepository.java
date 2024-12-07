package com.app.bank.repository;

import com.app.bank.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BankStatementRepository extends JpaRepository<BankStatement,Long> {
    List<BankStatement> findAllByAccountNumber(String accountNumber);
}
