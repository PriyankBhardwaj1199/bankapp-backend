package com.app.bank.controller;

import com.app.bank.utility.BankResponse;
import com.app.bank.utility.LoginStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public BankResponse handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return BankResponse.builder()
                .responseCode(LoginStatus.USER_NOT_FOUND.getCode())
                .responseMessage(LoginStatus.USER_NOT_FOUND.getMessage())
                .accountInfo(null)
                .build();
    }

    // You can add more exception handlers here as needed
}
