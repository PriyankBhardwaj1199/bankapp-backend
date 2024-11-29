package com.app.bank.service;

import com.app.bank.dto.*;
import com.app.bank.utility.BankResponse;

public interface UserService {

    BankResponse createAccount(UserDto userDto);

    BankResponse enquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditAccountRequest);

    BankResponse debitAccount(CreditDebitRequest debitAccountRequest);

    BankResponse transferRequest(TransferRequest transferRequest);

    BankResponse login(LoginDTO loginDTO);
}