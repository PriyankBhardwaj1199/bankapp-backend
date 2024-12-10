package com.app.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cards")
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String cardNumber;

    private int cvv;

    @JsonIgnore
    private int cardPin;

    private String name;

    private String pinStatus;

    private String cardType;

    private String cardSubType;

    private LocalDate issuedDate;

    private LocalDate expiryDate;

    private String accountNumber;

    private String cardStatus;
}
