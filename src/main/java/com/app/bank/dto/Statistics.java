package com.app.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {

    private long totalUsers;
    private long activeUsers;
    private long totalTransactions;
    private long pendingAccountApprovals;
    private long pendingCardApprovals;
    private long activeDebitCards;
    private long activeCreditCards;
    private long totalBankStatements;
}
