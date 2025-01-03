package com.app.bank.serviceImpl;

import com.app.bank.dto.CardsRequest;
import com.app.bank.entity.Cards;
import com.app.bank.repository.CardsRepository;
import com.app.bank.service.CardService;
import com.app.bank.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardsRepository cardsRepository;

    @Override
    public BankResponse applyCard(CardsRequest cardsRequest) {

        // check if the account already has the requested card
        Optional<Cards> card = cardsRepository.findByCardTypeAndAccountNumber(cardsRequest.getCardType(), cardsRequest.getAccountNumber());

        if(card.isPresent()){
           return BankResponse.builder()
                   .responseCode(CardsResponse.CARD_ALREADY_EXISTS.getCode())
                   .responseMessage(CardsResponse.CARD_ALREADY_EXISTS.getMessage())
                   .build();
        }

        // generate the card and save it to the database
        Cards newCard = AccountUtils.generateCard(cardsRequest);

        cardsRepository.save(newCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.CARD_APPLIED_SUCCESSFULLY.getCode())
                .responseMessage(CardsResponse.CARD_APPLIED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse generatePin(CardsRequest cardsRequest) {

        Optional<Cards> card = cardsRepository.findByCardNumberAndAccountNumber(cardsRequest.getCardNumber(), cardsRequest.getAccountNumber());

        if(card.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_NOT_FOUND.getCode())
                    .responseMessage(CardsResponse.CARD_NOT_FOUND.getMessage())
                    .build();
        }

        Cards userCard = card.get();

        BankResponse invalidCardResponse = AccountUtils.checkInvalidCard(userCard);

        if(!(invalidCardResponse==null)){
            return invalidCardResponse;
        }

        userCard.setCardPin(cardsRequest.getCardPin());
        userCard.setPinStatus(CardState.PIN_GENERATED.getDescription());

        cardsRepository.save(userCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.PIN_GENERATED.getCode())
                .responseMessage(CardsResponse.PIN_GENERATED.getMessage())
                .build();
    }

    @Override
    public BankResponse blockCard(CardsRequest cardsRequest) {

        Optional<Cards> card = cardsRepository.findByCardNumberAndAccountNumber(cardsRequest.getCardNumber(), cardsRequest.getAccountNumber());

        if(card.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_NOT_FOUND.getCode())
                    .responseMessage(CardsResponse.CARD_NOT_FOUND.getMessage())
                    .build();
        }

        Cards userCard = card.get();

        if(userCard.getCardStatus().equalsIgnoreCase(CardState.BLOCKED.getDescription())){
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_ALREADY_BLOCKED.getCode())
                    .responseMessage(CardsResponse.CARD_ALREADY_BLOCKED.getMessage())
                    .build();
        }

        BankResponse invalidCardResponse = AccountUtils.checkInvalidCard(userCard);

        if(!(invalidCardResponse==null)){
            return invalidCardResponse;
        }

        userCard.setCardStatus(CardState.BLOCKED.getDescription());
        cardsRepository.save(userCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.CARD_BLOCKED_SUCCESSFULLY.getCode())
                .responseMessage(CardsResponse.CARD_BLOCKED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public ResponseEntity<List<Cards>> getUserCards(String accountNumber) {
        return ResponseEntity.ok(cardsRepository.findAllByAccountNumber(accountNumber));
    }

    @Override
    public BankResponse activateCard(CardsRequest cardsRequest) {
        Optional<Cards> card = cardsRepository.findByCardNumberAndAccountNumber(cardsRequest.getCardNumber(), cardsRequest.getAccountNumber());

        if(card.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_NOT_FOUND.getCode())
                    .responseMessage(CardsResponse.CARD_NOT_FOUND.getMessage())
                    .build();
        }

        Cards userCard = card.get();

        if(userCard.getCardStatus().equalsIgnoreCase(CardState.ACTIVE.getDescription())){
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_ALREADY_ACTIVE.getCode())
                    .responseMessage(CardsResponse.CARD_ALREADY_ACTIVE.getMessage())
                    .build();
        }

        userCard.setCardStatus(CardState.ACTIVE.getDescription());
        cardsRepository.save(userCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.CARD_ACTIVATED_SUCCESSFULLY.getCode())
                .responseMessage(CardsResponse.CARD_ACTIVATED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse deActivateCard(CardsRequest cardsRequest) {
        Optional<Cards> card = cardsRepository.findByCardNumberAndAccountNumber(cardsRequest.getCardNumber(), cardsRequest.getAccountNumber());

        if(card.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_NOT_FOUND.getCode())
                    .responseMessage(CardsResponse.CARD_NOT_FOUND.getMessage())
                    .build();
        }

        Cards userCard = card.get();

        if(userCard.getCardStatus().equalsIgnoreCase(CardState.INACTIVE.getDescription())){
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_ALREADY_INACTIVE.getCode())
                    .responseMessage(CardsResponse.CARD_ALREADY_INACTIVE.getMessage())
                    .build();
        }

        userCard.setCardStatus(CardState.INACTIVE.getDescription());
        cardsRepository.save(userCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.CARD_DEACTIVATED_SUCCESSFULLY.getCode())
                .responseMessage(CardsResponse.CARD_DEACTIVATED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse deleteCard(CardsRequest cardsRequest) {
        Optional<Cards> card = cardsRepository.findByCardNumberAndAccountNumber(cardsRequest.getCardNumber(), cardsRequest.getAccountNumber());

        if(card.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_NOT_FOUND.getCode())
                    .responseMessage(CardsResponse.CARD_NOT_FOUND.getMessage())
                    .build();
        }

        Cards userCard = card.get();

        cardsRepository.delete(userCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.CARD_DELETED_SUCCESSFULLY.getCode())
                .responseMessage(CardsResponse.CARD_DELETED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse revokeCard(CardsRequest cardsRequest) {
        Optional<Cards> card = cardsRepository.findByCardNumberAndAccountNumber(cardsRequest.getCardNumber(), cardsRequest.getAccountNumber());

        if(card.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_NOT_FOUND.getCode())
                    .responseMessage(CardsResponse.CARD_NOT_FOUND.getMessage())
                    .build();
        }

        Cards userCard = card.get();

        if(userCard.getCardStatus().equalsIgnoreCase(CardState.REVOKED.getDescription())){
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_ALREADY_REVOKED.getCode())
                    .responseMessage(CardsResponse.CARD_ALREADY_REVOKED.getMessage())
                    .build();
        }

        userCard.setCardStatus(CardState.REVOKED.getDescription());
        cardsRepository.save(userCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.CARD_REVOKED_SUCCESSFULLY.getCode())
                .responseMessage(CardsResponse.CARD_REVOKED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse expireCard(CardsRequest cardsRequest) {
        Optional<Cards> card = cardsRepository.findByCardNumberAndAccountNumber(cardsRequest.getCardNumber(), cardsRequest.getAccountNumber());

        if(card.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_NOT_FOUND.getCode())
                    .responseMessage(CardsResponse.CARD_NOT_FOUND.getMessage())
                    .build();
        }

        Cards userCard = card.get();

        if(userCard.getCardStatus().equalsIgnoreCase(CardState.EXPIRED.getDescription())){
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_ALREADY_EXPIRED.getCode())
                    .responseMessage(CardsResponse.CARD_ALREADY_EXPIRED.getMessage())
                    .build();
        }

        userCard.setCardStatus(CardState.EXPIRED.getDescription());
        cardsRepository.save(userCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.CARD_EXPIRED_SUCCESSFULLY.getCode())
                .responseMessage(CardsResponse.CARD_EXPIRED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse unblockCard(CardsRequest cardsRequest) {
        Optional<Cards> card = cardsRepository.findByCardNumberAndAccountNumber(cardsRequest.getCardNumber(), cardsRequest.getAccountNumber());

        if(card.isEmpty()) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_NOT_FOUND.getCode())
                    .responseMessage(CardsResponse.CARD_NOT_FOUND.getMessage())
                    .build();
        }

        Cards userCard = card.get();

        if(userCard.getCardStatus().equalsIgnoreCase(CardState.ACTIVE.getDescription())){
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_ALREADY_ACTIVE.getCode())
                    .responseMessage(CardsResponse.CARD_ALREADY_ACTIVE.getMessage())
                    .build();
        }

        userCard.setCardStatus(CardState.ACTIVE.getDescription());
        cardsRepository.save(userCard);

        return BankResponse.builder()
                .responseCode(CardsResponse.CARD_ACTIVATED_SUCCESSFULLY.getCode())
                .responseMessage(CardsResponse.CARD_ACTIVATED_SUCCESSFULLY.getMessage())
                .build();
    }
}
