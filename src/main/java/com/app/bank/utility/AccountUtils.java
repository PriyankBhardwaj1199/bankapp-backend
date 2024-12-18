package com.app.bank.utility;

import com.app.bank.dto.CardsRequest;
import com.app.bank.entity.Cards;

import java.time.LocalDate;
import java.time.Year;
import java.util.Random;

public class AccountUtils {

    /**
     * Create an account number which starts with the name initials
     * followed by the current year followed by 8 random digits.
     */
    public static String generateAccountNumber(String initials){

        Year currentYear = Year.now();

        long minimum = 100000;
        long maximum = 999999;

        long randomNumber = (long) (Math.floor(Math.random()*(maximum-minimum + 1)+minimum));

        StringBuilder accountNumber = new StringBuilder();

        accountNumber.append(initials);
        accountNumber.append(String.valueOf(currentYear));
        accountNumber.append(String.valueOf(randomNumber));

        // generate the accountNumber and return
        return accountNumber.toString();

    }

    public static Cards generateCard(CardsRequest cardsRequest){

        String cardNumber = null;

        int cardLength = 16;


        if(cardsRequest.getCardSubType().equalsIgnoreCase(CardType.VISA.getName())){
            cardNumber = getCardNumber(CardType.VISA.getPrefix(),cardLength);
        } else if(cardsRequest.getCardSubType().equalsIgnoreCase(CardType.MASTERCARD.getName())){
            cardNumber = getCardNumber(CardType.MASTERCARD.getPrefix(),cardLength);
        } else if(cardsRequest.getCardSubType().equalsIgnoreCase(CardType.RUPAYCARD.getName())){
            cardNumber = getCardNumber(CardType.RUPAYCARD.getPrefix(),cardLength);
        }

        Random random = new Random();
        // Generate a 3-digit number (common for most cards)
        int cardCvv = random.nextInt(900) + 100;

        LocalDate issuedDate = LocalDate.now();
        LocalDate expiryDate = LocalDate.now().plusYears(5);

        return Cards.builder()
                .cardType(cardsRequest.getCardType())
                .cardSubType(cardsRequest.getCardSubType())
                .cvv(cardCvv)
                .name(cardsRequest.getName())
                .pinStatus(CardState.PIN_PENDING.getDescription())
                .cardStatus(CardState.PENDING_ACTIVATION.getDescription())
                .accountNumber(cardsRequest.getAccountNumber())
                .cardPin("")
                .cardNumber(cardNumber)
                .expiryDate(expiryDate)
                .issuedDate(issuedDate)
                .build();
}

    private static String getCardNumber(String prefix, int length) {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder(prefix);

        // Generate random digits to fill the card number up to length - 1
        while (cardNumber.length() < length - 1) {
            cardNumber.append(random.nextInt(10));
        }

        // Calculate the checksum digit
        int checksum = calculateLuhnChecksum(cardNumber.toString());
        cardNumber.append(checksum);

        return cardNumber.toString();
    }

    private static int calculateLuhnChecksum(String number) {
        int sum = 0;
        boolean isSecondDigit = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (isSecondDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            isSecondDigit = !isSecondDigit;
        }

        return (10 - (sum % 10)) % 10;
    }

    /**
     * Returns the bank response for invalid card
     * Returns null if card is valid
     */
    public static BankResponse checkInvalidCard(Cards card) {

        if(card.getCardStatus().equalsIgnoreCase(CardState.PENDING_ACTIVATION.getDescription())){
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_PENDING_APPROVAL.getCode())
                    .responseMessage(CardsResponse.CARD_PENDING_APPROVAL.getMessage())
                    .build();
        } else if (card.getCardStatus().equalsIgnoreCase(CardState.BLOCKED.getDescription())) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_BLOCKED.getCode())
                    .responseMessage(CardsResponse.CARD_BLOCKED.getMessage())
                    .build();
        } else if (card.getCardStatus().equalsIgnoreCase(CardState.EXPIRED.getDescription())) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_EXPIRED.getCode())
                    .responseMessage(CardsResponse.CARD_EXPIRED.getMessage())
                    .build();
        } else if (card.getCardStatus().equalsIgnoreCase(CardState.INACTIVE.getDescription())) {
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_INACTIVE.getCode())
                    .responseMessage(CardsResponse.CARD_INACTIVE.getMessage())
                    .build();
        } else if (card.getExpiryDate().isBefore(LocalDate.now())){
            return BankResponse.builder()
                    .responseCode(CardsResponse.CARD_EXPIRED.getCode())
                    .responseMessage(CardsResponse.CARD_EXPIRED.getMessage())
                    .build();
        }

        return null;
    }
}
