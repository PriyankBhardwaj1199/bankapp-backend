package com.app.bank.service;

import com.app.bank.dto.*;
import com.app.bank.entity.User;
import com.app.bank.utility.BankResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    BankResponse createAccount(UserDto userDto);

    BankResponse enquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditAccountRequest);

    BankResponse debitAccount(CreditDebitRequest debitAccountRequest);

    BankResponse transferRequest(TransferRequest transferRequest);

    BankResponse login(LoginDTO loginDTO);

    BankResponse deleteAccount(DeleteRequest deleteRequest);

    BankResponse updateAccount(UserDto userDto);

    BankResponse updatePassword(PasswordRequest passwordRequest);

    ResponseEntity<User> fetchUserAccount(String email);
}