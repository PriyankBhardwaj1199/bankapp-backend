package com.app.bank.controller;

import com.app.bank.dto.CreditDebitRequest;
import com.app.bank.dto.EnquiryRequest;
import com.app.bank.dto.TransferRequest;
import com.app.bank.dto.UserDto;
import com.app.bank.service.UserService;
import com.app.bank.utility.BankResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User account management API's.")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Create a new user account"
    )
    @ApiResponse(
            responseCode = "201",
            description = "New record created"
    )
    @PostMapping("/create")
    public BankResponse createAccount(@RequestBody UserDto userDto){
        return userService.createAccount(userDto);
    }

    @Operation(
            summary = "Enquiry for account information",
            description = "Enquire for account balance and other related information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Record retrieved successfully"
    )
    @GetMapping("/enquiry")
    public BankResponse getEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.enquiry(enquiryRequest);
    }

    @Operation(
            summary = "A credit transaction initiated",
            description = "Credits the required amount to an account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Amount credited to the account successfully"
    )
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest enquiryRequest){
        return userService.creditAccount(enquiryRequest);
    }

    @Operation(
            summary = "A debit transaction initiated",
            description = "Debits the required amount from an account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Amount debited from the account successfully"
    )
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest enquiryRequest){
        return userService.debitAccount(enquiryRequest);
    }

    @Operation(
            summary = "A transfer transaction initiated",
            description = "Credits the required amount to the destination account and debits from the source account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Amount transferred successfully"
    )
    @PostMapping("/transfer")
    public BankResponse transferAccount(@RequestBody TransferRequest transferRequest){
        return userService.transferRequest(transferRequest);
    }
}
