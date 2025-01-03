package com.app.bank.service;

import com.app.bank.dto.CardsRequest;
import com.app.bank.entity.Cards;
import com.app.bank.utility.BankResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CardService {
    BankResponse applyCard(CardsRequest cardsRequest);
    BankResponse generatePin(CardsRequest cardsRequest);
    BankResponse blockCard(CardsRequest cardsRequest);
    ResponseEntity<List<Cards>> getUserCards(String accountNumber);
    BankResponse activateCard(CardsRequest cardsRequest);
    BankResponse deActivateCard(CardsRequest cardsRequest);
    BankResponse deleteCard(CardsRequest cardsRequest);
    BankResponse revokeCard(CardsRequest cardsRequest);
    BankResponse expireCard(CardsRequest cardsRequest);
    BankResponse unblockCard(CardsRequest cardsRequest);
}
