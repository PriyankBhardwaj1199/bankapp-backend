package com.app.bank.serviceImpl;

import com.app.bank.config.JwtTokenProvider;
import com.app.bank.dto.*;
import com.app.bank.entity.User;
import com.app.bank.repository.TransactionRepository;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.EmailService;
import com.app.bank.service.TransactionService;
import com.app.bank.service.UserService;
import com.app.bank.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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

        String roleAssigned = null;

        if (userDto.getRole().equalsIgnoreCase("user")) {
            roleAssigned = Roles.USER.getRole();
        } else if (userDto.getRole().equalsIgnoreCase("admin")){
            roleAssigned = Roles.ADMIN.getRole();
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
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(roleAssigned)
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
                    .runningBalance(foundAccount.getAccountBalance())
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
                        .runningBalance(foundAccount.getAccountBalance())
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
        System.out.println(transferRequest);

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
                    .runningBalance(sourceAccount.getAccountBalance())
                    .build();

            TransactionDto transactionDtoReceiver = TransactionDto.builder()
                    .accountNumber(destinationAccount.getAccountNumber())
                    .transactionType(TransactionType.PAYMENT.getType())
                    .amount(transferRequest.getAmount())
                    .runningBalance(destinationAccount.getAccountBalance())
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

    @Override
    public BankResponse login(LoginDTO loginDTO) {

        Authentication authentication= null;
        JwtResponse jwtResponse = new JwtResponse();

        try {

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),loginDTO.getPassword()
                    )
            );

            if(authentication.isAuthenticated()){
                String jwtToken = jwtTokenProvider.generateToken(authentication);

                jwtResponse.setToken(jwtToken);

                userRepository.findByEmail(loginDTO.getUsername())
                        .ifPresent(value -> {
                            jwtResponse.setRoles(value.getRole());
                            jwtResponse.setUserName(value.getEmail());
                            jwtResponse.setAccountNumber(value.getAccountNumber());
                        });
            }

            return BankResponse.builder()
                    .responseCode(LoginStatus.SUCCESS.getCode())
                    .responseMessage(LoginStatus.SUCCESS.getMessage())
                    .jwtResponse(jwtResponse)
                    .build();
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            return BankResponse.builder()
                    .responseCode(LoginStatus.INVALID_CREDENTIALS.getCode()) // Set a failure code
                    .responseMessage(LoginStatus.INVALID_CREDENTIALS.getMessage()) // Notify the user
                    .build();

        } catch (UsernameNotFoundException e) {
            // Handle user not found
            throw e;  // Re-throw to be caught by @ControllerAdvice
        } catch (AuthenticationException e) {
            // Handle any other authentication-related exception
            return BankResponse.builder()
                    .responseCode(LoginStatus.FAILURE.getCode()) // Set a failure code
                    .responseMessage(LoginStatus.FAILURE.getMessage()) // General error message
                    .build();
        }


    }

    @Override
    public BankResponse deleteAccount(DeleteRequest deleteRequest) {

        // check if account is present or not
        if(!userRepository.existsByEmail(deleteRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(DeleteAccountResponse.ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(DeleteAccountResponse.ACCOUNT_NOT_FOUND.getMessage())
                    .accountInfo(null)
                    .build();
        }

        User deletedUser = userRepository.findByAccountNumber(deleteRequest.getAccountNumber());

        int recordDeleted = userRepository.deleteByAccountNumber(deleteRequest.getAccountNumber());

        if(recordDeleted == 1){

            transactionRepository.deleteByAccountNumber(deleteRequest.getAccountNumber());

            String mailBody = """
Dear %s,

We hope this message finds you well.

This is to inform you that your account associated with the account number %s has been successfully deleted from our system. If this action was intentional, no further steps are needed.

If you believe this action was in error or have any concerns, please contact us immediately at bankingsystem.email.com.

Thank you for your association with us.

Best regards,
Banking System
""".formatted(deletedUser.getFirstName()+" "+deletedUser.getMiddleName()+" "+deletedUser.getLastName(),
                    deletedUser.getAccountNumber());

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(deleteRequest.getEmail())
                    .subject("ACCOUNT DELETED")
                    .message(mailBody)
                    .build();

            emailService.sendEmailAlert(emailDetails);

            return BankResponse.builder()
                    .responseCode(DeleteAccountResponse.SUCCESS.getCode())
                    .responseMessage(DeleteAccountResponse.SUCCESS.getMessage())
                    .accountInfo(null)
                    .build();
        }

        return BankResponse.builder()
                .responseCode(DeleteAccountResponse.SERVER_ERROR.getCode())
                .responseMessage(DeleteAccountResponse.SERVER_ERROR.getMessage())
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse updateAccount(UserDto userDto) {

        // check if the user is present in the database
        if(!userRepository.existsByEmail(userDto.getEmail())){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getMessage())
                    .accountInfo(null)
                    .build();
        }

        Optional<User> userFromDatabase = userRepository.findByEmail(userDto.getEmail());

        if(userFromDatabase.isPresent()){
            User userToUpdate = userFromDatabase.get();

            userToUpdate.setFirstName(userDto.getFirstName());
            userToUpdate.setLastName(userDto.getLastName());
            userToUpdate.setMiddleName(userDto.getMiddleName());
            userToUpdate.setGender(userDto.getGender());
            userToUpdate.setAddressLine1(userDto.getAddressLine1());
            userToUpdate.setAddressLine2(userDto.getAddressLine2());
            userToUpdate.setCity(userDto.getCity());
            userToUpdate.setStateOfOrigin(userDto.getStateOfOrigin());
            userToUpdate.setPinCode(userDto.getPinCode());
            userToUpdate.setCountry(userDto.getCountry());
            userToUpdate.setPhoneNumber(userDto.getPhoneNumber());
            userToUpdate.setAlternativePhoneNumber(userDto.getAlternativePhoneNumber());

            userRepository.save(userToUpdate);
        }

        return BankResponse.builder()
                .responseCode(UpdateAccountResponse.SUCCESS.getCode())
                .responseMessage(UpdateAccountResponse.SUCCESS.getMessage())
                .accountInfo(null)
                .build();
    }

    @Override
    public BankResponse updatePassword(PasswordRequest passwordRequest) {
        // check if the user is present in the database
        if(!userRepository.existsByAccountNumber(passwordRequest.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getMessage())
                    .accountInfo(null)
                    .build();
        }

        Optional<User> userFromDatabase = Optional.ofNullable(userRepository.findByAccountNumber(passwordRequest.getAccountNumber()));

        if(userFromDatabase.isPresent()){
            User userToUpdate = userFromDatabase.get();

            if(passwordEncoder.matches(passwordRequest.getOldPassword(),userToUpdate.getPassword())){

                userToUpdate.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

                userRepository.save(userToUpdate);

                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(userToUpdate.getEmail())
                        .subject("PASSWORD UPDATED")
                        .message(
                                "Dear " + userToUpdate.getFirstName() + ",\n\n" +
                                        "We wanted to let you know that your account password has been successfully updated. If you did not make this change or believe this to be an error, please contact our support team immediately at support@bankingsystem.com.\n\n" +
                                        "Important: For your security, we recommend that you review your account settings and ensure that all information is up-to-date.\n\n" +
                                        "If you need further assistance or wish to change your password again, you can do so by visiting our account settings.\n\n" +
                                        "Thank you for being a valued member of Banking System.\n\n" +
                                        "Best regards,\n" +
                                        "The Banking System Team\n" +
                                        "support@bankingsystem.com\n\n" +
                                        "Security Reminder:\n" +
                                        "If you suspect unauthorized activity on your account, please change your password immediately for security."
                        )
                        .build();

                emailService.sendEmailAlert(emailDetails);

                return BankResponse.builder()
                        .responseCode(UpdateAccountResponse.SUCCESS.getCode())
                        .responseMessage(UpdateAccountResponse.SUCCESS.getMessage())
                        .accountInfo(null)
                        .build();
            } else {
                return BankResponse.builder()
                        .responseCode(UpdateAccountResponse.PASSWORD_MISMATCH.getCode())
                        .responseMessage(UpdateAccountResponse.PASSWORD_MISMATCH.getMessage())
                        .accountInfo(null)
                        .build();
            }

        }

        return BankResponse.builder()
                .responseCode(UpdateAccountResponse.SERVER_ERROR.getCode())
                .responseMessage(UpdateAccountResponse.SERVER_ERROR.getMessage())
                .accountInfo(null)
                .build();
    }

    @Override
    public ResponseEntity<User> fetchUserAccount(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        return user.map(ResponseEntity::ok).orElse(null);
    }
}
