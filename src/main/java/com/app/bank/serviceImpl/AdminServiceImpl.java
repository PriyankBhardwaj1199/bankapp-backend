package com.app.bank.serviceImpl;

import com.app.bank.entity.Cards;
import com.app.bank.entity.User;
import com.app.bank.repository.CardsRepository;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.AdminService;
import com.app.bank.utility.CardState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardsRepository cardsRepository;

    @Override
    public ResponseEntity<List<User>> fetchAllUserAccounts() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Override
    public ResponseEntity<List<Cards>> fetchAllPendingCards() {
        return ResponseEntity.ok(cardsRepository.findAllByCardStatus(CardState.PENDING_ACTIVATION.getDescription()));
    }
}
