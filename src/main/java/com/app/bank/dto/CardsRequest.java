package com.app.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardsRequest {

    private String cardNumber;

    private int cvv;

    private String cardPin;

    private String cardType;

    private String cardSubType;

    private String name;

    private LocalDate issuedDate;

    private LocalDate expiryDate;

    private String accountNumber;
}
