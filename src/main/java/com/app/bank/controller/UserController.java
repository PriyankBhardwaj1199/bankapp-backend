package com.app.bank.controller;

import com.app.bank.dto.*;
import com.app.bank.entity.User;
import com.app.bank.service.UserService;
import com.app.bank.utility.BankResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User account management API's.")
@CrossOrigin(origins = "http://localhost:4200")
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

    @Operation(
            summary = "A login attempt initiated",
            description = "User initiated a login attempt to his/her account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Logged in successfully"
    )
    @PostMapping("/login")
    public BankResponse loginAccount(@RequestBody LoginDTO loginDTO){
            return userService.login(loginDTO);
    }

    @Operation(
            summary = "Delete a user account",
            description = "Deletes an account and its related transactions from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Record deleted successfully"
    )
    @DeleteMapping("/delete")
    public BankResponse deleteAccount(@RequestBody DeleteRequest deleteRequest){
        return userService.deleteAccount(deleteRequest);
    }

    @Operation(
            summary = "Update the user account",
            description = "Updates an account of the user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Record updated successfully"
    )
    @PatchMapping("/update")
    public BankResponse updateAccount(@RequestBody UserDto userDto){
        return userService.updateAccount(userDto);
    }

    @Operation(
            summary = "Update the user account's password",
            description = "Updates the password of the account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Password updated successfully"
    )
    @PatchMapping("/passwordUpdate")
    public BankResponse updatePassword(@RequestBody PasswordRequest passwordRequest){
        return userService.updatePassword(passwordRequest);
    }

    @Operation(
            summary = "Fetch the user account's information",
            description = "Returns the user account information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account information fetched successfully"
    )
    @GetMapping("/fetchAccount/{email}")
    public ResponseEntity<User> getLoggedInUserAccountInfo(@PathVariable String email){
        return userService.fetchUserAccount(email);
    }
}
