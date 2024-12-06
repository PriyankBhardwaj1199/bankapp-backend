package com.app.bank.service;

import com.app.bank.entity.Cards;
import com.app.bank.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<List<User>> fetchAllUserAccounts();

    ResponseEntity<List<Cards>> fetchAllPendingCards();
}