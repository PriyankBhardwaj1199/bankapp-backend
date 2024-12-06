package com.app.bank.serviceImpl;

import com.app.bank.dto.CardsRequest;
import com.app.bank.entity.Cards;
import com.app.bank.entity.User;
import com.app.bank.repository.CardsRepository;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.AdminService;
import com.app.bank.service.CardService;
import com.app.bank.utility.BankResponse;
import com.app.bank.utility.CardState;
import com.app.bank.utility.CardsResponse;
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

    @Autowired
    private CardService cardService;

    @Override
    public ResponseEntity<List<User>> fetchAllUserAccounts() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Override
    public ResponseEntity<List<Cards>> fetchAllPendingCards() {
        return ResponseEntity.ok(cardsRepository.findAllByCardStatus(CardState.PENDING_ACTIVATION.getDescription()));
    }

    @Override
    public BankResponse actionOnCard(CardsRequest cardsRequest, String action) {

        switch (action) {
            case "BLOCK" -> {
                return cardService.blockCard(cardsRequest);
            }
            case "ACTIVATE" -> {
                return cardService.activateCard(cardsRequest);
            }
            case "INACTIVATE" -> {
                return cardService.deActivateCard(cardsRequest);
            }
            case "DELETE" -> {
                return cardService.deleteCard(cardsRequest);
            }
            case "REVOKE" -> {
                return cardService.revokeCard(cardsRequest);
            }
            case "EXPIRE" -> {
                return cardService.expireCard(cardsRequest);
            }
            default -> {
                return BankResponse.builder()
                        .responseCode(CardsResponse.INTERNAL_SERVER_ERROR.getCode())
                        .responseMessage(CardsResponse.INTERNAL_SERVER_ERROR.getMessage())
                        .build();
            }
        }


    }
}
