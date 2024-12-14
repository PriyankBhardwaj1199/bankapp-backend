package com.app.bank.repository;

import com.app.bank.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BankStatementRepository extends JpaRepository<BankStatement,Long> {
    List<BankStatement> findAllByAccountNumberOrderByCreatedOnDesc(String accountNumber);

    @Query("SELECT b.pdfFile FROM BankStatement b WHERE b.id = :id AND b.accountNumber = :accountNumber")
    Optional<byte[]> getPdfFileByIdAndAccountNumber(@Param("id") Long id,@Param("accountNumber") String accountNumber);
}
