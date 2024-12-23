package com.app.bank.repository;

import com.app.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndAccountNumber(String email,String accountNumber);

    int deleteByAccountNumber(String accountNumber);

    @Query("SELECT u.accountBalance FROM User u WHERE u.accountNumber = :accountNumber")
    Optional<BigDecimal> findAccountBalanceByAccountNumber(@Param("accountNumber") String accountNumber);

    long countByStatus(String status);

}
