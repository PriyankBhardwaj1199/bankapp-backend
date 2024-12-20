package com.app.bank.service;

import com.app.bank.dto.CardsRequest;
import com.app.bank.dto.Statistics;
import com.app.bank.entity.Cards;
import com.app.bank.entity.User;
import com.app.bank.utility.BankResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<List<User>> fetchAllUserAccounts();
    ResponseEntity<List<Cards>> fetchAllCards();
    BankResponse actionOnCard(CardsRequest cardsRequest, String action);
    Statistics fetchStatistics();
}
