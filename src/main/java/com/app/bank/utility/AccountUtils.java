package com.app.bank.utility;

import java.time.Year;

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
}
