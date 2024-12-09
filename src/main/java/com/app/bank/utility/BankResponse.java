package com.app.bank.utility;

import com.app.bank.dto.AccountInfo;
import com.app.bank.dto.JwtResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
    private int responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;
    private JwtResponse jwtResponse;
}
