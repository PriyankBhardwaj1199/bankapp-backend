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

    // Statistics related to account and users.
    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long suspendedUsers;
    private long closedAccounts;
    private long pendingAccountApprovals;
    private long deactivatedAccounts;

    private long totalTransactions;

    // cards related statistics
    private long pendingCardApprovals;
    private long inactiveCreditCards;
    private long inactiveDebitCards;
    private long activeDebitCards;
    private long activeCreditCards;
    private long expiredCards;
    private long revokedCards;
    private long totalCards;

    private long totalBankStatements;
}
