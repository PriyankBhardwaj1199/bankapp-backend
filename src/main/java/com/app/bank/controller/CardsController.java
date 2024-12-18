package com.app.bank.controller;

import com.app.bank.dto.CardsRequest;
import com.app.bank.entity.Cards;
import com.app.bank.service.CardService;
import com.app.bank.utility.BankResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Card API.")
@CrossOrigin(origins = "http://localhost:4200")
public class CardsController {

    @Autowired
    private CardService cardService;

    @Operation(
            summary = "Apply for a new card",
            description = "Let's user apply for a new card"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Application for card successful"
    )
    @PostMapping("/apply")
    public BankResponse applyCard(@RequestBody CardsRequest cardsRequest){
        return cardService.applyCard(cardsRequest);
    }

    @Operation(
            summary = "Get all user card",
            description = "Fetches current cards for user account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Cards fetched successful"
    )
    @GetMapping("/get-cards/{accountNumber}")
    public ResponseEntity<List<Cards>> getCardForUser(@PathVariable String accountNumber){
        return cardService.getUserCards(accountNumber);
    }

    @Operation(
            summary = "Create a pin for the Card",
            description = "Let's user apply for card pin"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Application for card successful"
    )
    @PostMapping("/generate-pin")
    public BankResponse generatePin(@RequestBody CardsRequest cardsRequest){
        return cardService.generatePin(cardsRequest);
    }

    @Operation(
            summary = "Block card",
            description = "Let's user block card"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Card block successful"
    )
    @PostMapping("/block")
    public BankResponse blockCard(@RequestBody CardsRequest cardsRequest) {
        return cardService.blockCard(cardsRequest);
    }

    @Operation(
            summary = "Block card",
            description = "Let's user block card"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Card block successful"
    )
    @PostMapping("/unblock")
    public BankResponse unblockCard(@RequestBody CardsRequest cardsRequest) {
        return cardService.unblockCard(cardsRequest);
    }
}
