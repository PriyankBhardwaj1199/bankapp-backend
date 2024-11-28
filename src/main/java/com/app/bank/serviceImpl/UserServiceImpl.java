package com.app.bank.serviceImpl;

import com.app.bank.dto.*;
import com.app.bank.entity.User;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.EmailService;
import com.app.bank.service.TransactionService;
import com.app.bank.service.UserService;
import com.app.bank.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

    /**
     * Create an account - save the info of the new user in database
     */
    @Override
    public BankResponse createAccount(UserDto userDto) {

        // check if user already has an account

        if(userRepository.existsByEmail(userDto.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountCreationResponse.DUPLICATE_ACCOUNT.getCode())
                    .responseMessage(AccountCreationResponse.DUPLICATE_ACCOUNT.getMessage())
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .middleName(userDto.getMiddleName())
                .gender(userDto.getGender())
                .addressLine1(userDto.getAddressLine1())
                .addressLine2(userDto.getAddressLine2())
                .city(userDto.getCity())
                .stateOfOrigin(userDto.getStateOfOrigin())
                .pinCode(userDto.getPinCode())
                .country(userDto.getCountry())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .alternativePhoneNumber(userDto.getAlternativePhoneNumber())
                .status(AccountStatus.ACTIVE.getDescription())
                .accountNumber(AccountUtils.generateAccountNumber(userDto.getFirstName().substring(0, 1).toUpperCase()
                        +userDto.getLastName().substring(0, 1).toUpperCase()))
                .accountBalance(BigDecimal.ZERO)
                .build();

        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .message(String.format("Congratulations!!! You have successfully created a new account.\n" +
                                "Your account details are:\n" +
                                "Account Number: %s\n" +
                                "Account Balance: %s\n" +
                                "First Name: %s\n" +
                                "Last Name: %s\n" +
                                "Middle Name: %s\n" +
                                "Gender: %s\n" +
                                "Address Line 1: %s\n" +
                                "Address Line 2: %s\n" +
                                "City: %s\n" +
                                "State of Origin: %s\n" +
                                "Pin Code: %s\n" +
                                "Country: %s\n" +
                                "Email: %s\n" +
                                "Phone Number: %s\n" +
                                "Alternative Phone Number: %s",
                        savedUser.getAccountNumber(),
                        savedUser.getAccountBalance().toString(),
                        savedUser.getFirstName(),
                        savedUser.getLastName(),
                        savedUser.getMiddleName(),
                        savedUser.getGender(),
                        savedUser.getAddressLine1(),
                        savedUser.getAddressLine2(),
                        savedUser.getCity(),
                        savedUser.getStateOfOrigin(),
                        savedUser.getPinCode(),
                        savedUser.getCountry(),
                        savedUser.getEmail(),
                        savedUser.getPhoneNumber(),
                        savedUser.getAlternativePhoneNumber()))
                .build();


        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountCreationResponse.SUCCESS.getCode())
                .responseMessage(AccountCreationResponse.SUCCESS.getMessage())
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "+ savedUser.getMiddleName() +" "+savedUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse enquiry(EnquiryRequest enquiryRequest) {

        if(userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber())){

            User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

            return BankResponse.builder()
                    .responseCode(BalanceEnquiryResponse.SUCCESS.getCode())
                    .responseMessage(BalanceEnquiryResponse.SUCCESS.getMessage())
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundUser.getFirstName()+ " " + foundUser.getMiddleName() + " " + foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .build();
        }

        return BankResponse.builder()
                .responseCode(BalanceEnquiryResponse.ACCOUNT_NOT_FOUND.getCode())
                .responseMessage(BalanceEnquiryResponse.ACCOUNT_NOT_FOUND.getMessage())
                .accountInfo(null)
                .build();
    }

    private Boolean checkAccountExists(CreditDebitRequest enquiryRequest) {
        return userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditAccountRequest) {

        if(checkAccountExists(creditAccountRequest)){
            User foundAccount = userRepository.findByAccountNumber(creditAccountRequest.getAccountNumber());

            foundAccount.setAccountBalance(foundAccount.getAccountBalance().add(creditAccountRequest.getAmount()));

            userRepository.save(foundAccount);

            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(foundAccount.getAccountNumber())
                    .transactionType(TransactionType.DEPOSIT.getType())
                    .amount(creditAccountRequest.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDto);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(foundAccount.getEmail())
                    .subject("CREDIT TRANSACTION")
                    .message(String.format("Hi! Your account has been credited with Rupees " + creditAccountRequest.getAmount() +
                                    ". \nYour account details are:\n" +
                                    "Account Number: %s\n" +
                                    "Updated Account Balance: %s\n" +
                                    "Name: %s\n",
                            foundAccount.getAccountNumber(),
                            foundAccount.getAccountBalance().toString(),
                            foundAccount.getFirstName()+" "+foundAccount.getMiddleName()+" "+foundAccount.getLastName()+"\n"+
                            "Thank you"
                            ))
                    .build();


            emailService.sendEmailAlert(emailDetails);

            return BankResponse.builder()
                    .responseCode(BalanceEnquiryResponse.SUCCESS.getCode())
                    .responseMessage(BalanceEnquiryResponse.SUCCESS.getMessage())
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundAccount.getFirstName()+ " " + foundAccount.getMiddleName() + " " + foundAccount.getLastName())
                            .accountNumber(foundAccount.getAccountNumber())
                            .accountBalance(foundAccount.getAccountBalance())
                            .build())
                    .build();
        }

        return BankResponse.builder()
                .responseCode(BalanceEnquiryResponse.ACCOUNT_NOT_FOUND.getCode())
                .responseMessage(BalanceEnquiryResponse.ACCOUNT_NOT_FOUND.getMessage())
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest debitAccountRequest) {
        if(checkAccountExists(debitAccountRequest)){
            User foundAccount = userRepository.findByAccountNumber(debitAccountRequest.getAccountNumber());

            if(foundAccount.getAccountBalance().compareTo(debitAccountRequest.getAmount())>=0){

                foundAccount.setAccountBalance(foundAccount.getAccountBalance().subtract(debitAccountRequest.getAmount()));

                userRepository.save(foundAccount);

                TransactionDto transactionDto = TransactionDto.builder()
                        .accountNumber(foundAccount.getAccountNumber())
                        .transactionType(TransactionType.WITHDRAWAL.getType())
                        .amount(debitAccountRequest.getAmount())
                        .build();

                transactionService.saveTransaction(transactionDto);

                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(foundAccount.getEmail())
                        .subject("DEBIT TRANSACTION")
                        .message(String.format("Hi! Your account has been debited with Rupees " + debitAccountRequest.getAmount() +
                                        ". \nYour account details are:\n" +
                                        "Account Number: %s\n" +
                                        "Updated Account Balance: %s\n" +
                                        "Name: %s\n",
                                foundAccount.getAccountNumber(),
                                foundAccount.getAccountBalance().toString(),
                                foundAccount.getFirstName()+" "+foundAccount.getMiddleName()+" "+foundAccount.getLastName()+"\n"+
                                        "Thank you"
                        ))
                        .build();


                emailService.sendEmailAlert(emailDetails);

                return BankResponse.builder()
                        .responseCode(BalanceEnquiryResponse.SUCCESS.getCode())
                        .responseMessage(BalanceEnquiryResponse.SUCCESS.getMessage())
                        .accountInfo(AccountInfo.builder()
                                .accountName(foundAccount.getFirstName()+ " " + foundAccount.getMiddleName() + " " + foundAccount.getLastName())
                                .accountNumber(foundAccount.getAccountNumber())
                                .accountBalance(foundAccount.getAccountBalance())
                                .build())
                        .build();
            } else {

                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(foundAccount.getEmail())
                        .subject("FAILED DEBIT TRANSACTION")
                        .message(String.format("Hi! Your transaction for Rupees " + debitAccountRequest.getAmount() +
                                        " failed due to insufficient balance. \n Transaction details are:\n" +
                                        "Name: %s\n"+
                                        "Account Number: %s\n" +
                                        "Account Balance: %s\n" +
                                        "Requested Amount: %s\n",
                                foundAccount.getFirstName()+" "+foundAccount.getMiddleName()+" "+foundAccount.getLastName(),
                                foundAccount.getAccountNumber(),
                                foundAccount.getAccountBalance().toString(),
                                debitAccountRequest.getAmount()+"\n"+
                                        "Thank you"
                        ))
                        .build();


                emailService.sendEmailAlert(emailDetails);

                return BankResponse.builder()
                        .responseCode(BalanceEnquiryResponse.INVALID_ENQUIRY.getCode())
                        .responseMessage(BalanceEnquiryResponse.INVALID_ENQUIRY.getMessage())
                        .accountInfo(AccountInfo.builder()
                                .accountName(foundAccount.getFirstName()+ " " + foundAccount.getMiddleName() + " " + foundAccount.getLastName())
                                .accountNumber(foundAccount.getAccountNumber())
                                .accountBalance(foundAccount.getAccountBalance())
                                .build())
                        .build();
            }


        }

        return BankResponse.builder()
                .responseCode(BalanceEnquiryResponse.ACCOUNT_NOT_FOUND.getCode())
                .responseMessage(BalanceEnquiryResponse.ACCOUNT_NOT_FOUND.getMessage())
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse transferRequest(TransferRequest transferRequest) {

        // check existence of source account and destination account

        Boolean isSourceAccountExist = userRepository.existsByAccountNumber(transferRequest.getSourceAccountNumber());
        Boolean isDestinationAccountExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());

        // if destination account doesn't exist
        if(!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(TransferResponse.DESTINATION_ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(TransferResponse.DESTINATION_ACCOUNT_NOT_FOUND.getMessage())
                    .accountInfo(null)
                    .build();
        }

        // if source account doesn't exist
        if(!isSourceAccountExist) {
            return BankResponse.builder()
                    .responseCode(TransferResponse.SOURCE_ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(TransferResponse.SOURCE_ACCOUNT_NOT_FOUND.getMessage())
                    .accountInfo(null)
                    .build();
        }

        User sourceAccount = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        User destinationAccount = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());

        if(sourceAccount.getAccountBalance().compareTo(transferRequest.getAmount()) >= 0) {
            sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferRequest.getAmount()));
            destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transferRequest.getAmount()));

            userRepository.save(sourceAccount);
            userRepository.save(destinationAccount);

            TransactionDto transactionDtoSender = TransactionDto.builder()
                    .accountNumber(sourceAccount.getAccountNumber())
                    .transactionType(TransactionType.TRANSFER.getType())
                    .amount(transferRequest.getAmount())
                    .build();

            TransactionDto transactionDtoReceiver = TransactionDto.builder()
                    .accountNumber(destinationAccount.getAccountNumber())
                    .transactionType(TransactionType.PAYMENT.getType())
                    .amount(transferRequest.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDtoSender);
            transactionService.saveTransaction(transactionDtoReceiver);

            return BankResponse.builder()
                    .responseCode(TransferResponse.SUCCESS.getCode())
                    .responseMessage(TransferResponse.SUCCESS.getMessage())
                    .accountInfo(AccountInfo.builder()
                            .accountName(sourceAccount.getFirstName()+ " " + sourceAccount.getMiddleName() + " " + sourceAccount.getLastName())
                            .accountNumber(sourceAccount.getAccountNumber())
                            .accountBalance(sourceAccount.getAccountBalance())
                            .build())
                    .build();
        }

        return BankResponse.builder()
                .responseCode(TransferResponse.INSUFFICIENT_BALANCE.getCode())
                .responseMessage(TransferResponse.INSUFFICIENT_BALANCE.getMessage())
                .accountInfo(AccountInfo.builder()
                        .accountName(sourceAccount.getFirstName()+ " " + sourceAccount.getMiddleName() + " " + sourceAccount.getLastName())
                        .accountNumber(sourceAccount.getAccountNumber())
                        .accountBalance(sourceAccount.getAccountBalance())
                        .build())
                .build();
    }


}
