package com.app.bank.serviceImpl;

import com.app.bank.dto.FetchAccount;
import com.app.bank.entity.User;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.AccountService;
import com.app.bank.utility.AccountStatus;
import com.app.bank.utility.BankResponse;
import com.app.bank.utility.UpdateAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public BankResponse activateAccount(FetchAccount account) {

        Optional<User> intendedUser = userRepository.findByEmailAndAccountNumber(account.getEmail(), account.getAccountNumber());

        if(intendedUser.isEmpty()){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getMessage())
                    .build();
        }

        User user = intendedUser.get();

        if(user.getStatus().equalsIgnoreCase(AccountStatus.ACTIVE.getDescription())){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_ALREADY_ACTIVATED.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_ALREADY_ACTIVATED.getMessage())
                    .build();
        }

        user.setStatus(AccountStatus.ACTIVE.getDescription());

        userRepository.save(user);

        return BankResponse.builder()
                .responseCode(UpdateAccountResponse.ACCOUNT_ACTIVATED_SUCCESSFULLY.getCode())
                .responseMessage(UpdateAccountResponse.ACCOUNT_ACTIVATED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse deActivateAccount(FetchAccount account) {
        Optional<User> intendedUser = userRepository.findByEmailAndAccountNumber(account.getEmail(), account.getAccountNumber());

        if(intendedUser.isEmpty()){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getMessage())
                    .build();
        }

        User user = intendedUser.get();

        if(user.getStatus().equalsIgnoreCase(AccountStatus.INACTIVE.getDescription())){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_ALREADY_DEACTIVATED.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_ALREADY_DEACTIVATED.getMessage())
                    .build();
        }

        user.setStatus(AccountStatus.INACTIVE.getDescription());

        userRepository.save(user);

        return BankResponse.builder()
                .responseCode(UpdateAccountResponse.ACCOUNT_DEACTIVATED_SUCCESSFULLY.getCode())
                .responseMessage(UpdateAccountResponse.ACCOUNT_DEACTIVATED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse deleteAccount(FetchAccount account) {
        Optional<User> intendedUser = userRepository.findByEmailAndAccountNumber(account.getEmail(), account.getAccountNumber());

        if(intendedUser.isEmpty()){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getMessage())
                    .build();
        }

        User user = intendedUser.get();

        userRepository.delete(user);

        return BankResponse.builder()
                .responseCode(UpdateAccountResponse.ACCOUNT_DELETED_SUCCESSFULLY.getCode())
                .responseMessage(UpdateAccountResponse.ACCOUNT_DELETED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse suspendAccount(FetchAccount account) {
        Optional<User> intendedUser = userRepository.findByEmailAndAccountNumber(account.getEmail(), account.getAccountNumber());

        if(intendedUser.isEmpty()){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getMessage())
                    .build();
        }

        User user = intendedUser.get();

        if(user.getStatus().equalsIgnoreCase(AccountStatus.SUSPENDED.getDescription())){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_ALREADY_SUSPENDED.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_ALREADY_SUSPENDED.getMessage())
                    .build();
        }

        user.setStatus(AccountStatus.SUSPENDED.getDescription());

        userRepository.save(user);

        return BankResponse.builder()
                .responseCode(UpdateAccountResponse.ACCOUNT_SUSPENDED_SUCCESSFULLY.getCode())
                .responseMessage(UpdateAccountResponse.ACCOUNT_SUSPENDED_SUCCESSFULLY.getMessage())
                .build();
    }

    @Override
    public BankResponse closeAccount(FetchAccount account) {
        Optional<User> intendedUser = userRepository.findByEmailAndAccountNumber(account.getEmail(), account.getAccountNumber());

        if(intendedUser.isEmpty()){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_NOT_FOUND.getMessage())
                    .build();
        }

        User user = intendedUser.get();

        if(user.getStatus().equalsIgnoreCase(AccountStatus.CLOSED.getDescription())){
            return BankResponse.builder()
                    .responseCode(UpdateAccountResponse.ACCOUNT_ALREADY_CLOSED.getCode())
                    .responseMessage(UpdateAccountResponse.ACCOUNT_ALREADY_CLOSED.getMessage())
                    .build();
        }

        user.setStatus(AccountStatus.CLOSED.getDescription());

        userRepository.save(user);

        return BankResponse.builder()
                .responseCode(UpdateAccountResponse.ACCOUNT_CLOSED_SUCCESSFULLY.getCode())
                .responseMessage(UpdateAccountResponse.ACCOUNT_CLOSED_SUCCESSFULLY.getMessage())
                .build();
    }
}
