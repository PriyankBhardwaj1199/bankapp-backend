package com.app.bank.repository;

import com.app.bank.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CardsRepository extends JpaRepository<Cards,String> {

    Optional<Cards> findByCardTypeAndAccountNumber(String cardType, String accountNumber);

    Optional<Cards> findByCardNumberAndAccountNumber(String cardNumber, String accountNumber);

    List<Cards> findAllByAccountNumber(String accountNumber);

    long countByCardStatus(String cardStatus);

    long countByCardTypeAndCardStatus(String cardType, String cardStatus);

}
