package com.app.bank.service;

import com.app.bank.dto.FetchAccount;
import com.app.bank.utility.BankResponse;

public interface AccountService {
    BankResponse activateAccount(FetchAccount account);

    BankResponse deActivateAccount(FetchAccount account);

    BankResponse deleteAccount(FetchAccount account);

    BankResponse suspendAccount(FetchAccount account);

    BankResponse closeAccount(FetchAccount account);
}
