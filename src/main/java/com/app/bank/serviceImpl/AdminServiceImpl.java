package com.app.bank.serviceImpl;

import com.app.bank.dto.CardsRequest;
import com.app.bank.dto.Statistics;
import com.app.bank.entity.Cards;
import com.app.bank.entity.User;
import com.app.bank.repository.BankStatementRepository;
import com.app.bank.repository.CardsRepository;
import com.app.bank.repository.TransactionRepository;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.AdminService;
import com.app.bank.service.CardService;
import com.app.bank.utility.*;
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
    private TransactionRepository transactionRepository;

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Autowired
    private CardService cardService;

    @Override
    public ResponseEntity<List<User>> fetchAllUserAccounts() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Override
    public ResponseEntity<List<Cards>> fetchAllCards() {
        return ResponseEntity.ok(cardsRepository.findAll());
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

    @Override
    public Statistics fetchStatistics() {
        Statistics stats = new Statistics();

        stats.setActiveUsers(userRepository.countByStatus(AccountStatus.ACTIVE.getDescription()));
        stats.setTotalUsers(userRepository.count());
        stats.setPendingAccountApprovals(userRepository.countByStatus(AccountStatus.PENDING.getDescription()));
        stats.setPendingCardApprovals(cardsRepository.countByCardStatus(CardState.PENDING_ACTIVATION.getDescription()));
        stats.setTotalTransactions(transactionRepository.count());
        stats.setActiveDebitCards(cardsRepository.countByCardTypeAndCardStatus(CardType.DEBIT.getName(),CardState.ACTIVE.getDescription()));
        stats.setActiveCreditCards(cardsRepository.countByCardTypeAndCardStatus(CardType.CREDIT.getName(), CardState.ACTIVE.getDescription()));
        stats.setTotalBankStatements(bankStatementRepository.count());
        return stats;
    }
}
