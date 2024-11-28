package com.app.bank.utility;

public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    PAYMENT("Payment"),
    REFUND("Refund"),
    LOAN_DISBURSEMENT("Loan Disbursement"),
    LOAN_REPAYMENT("Loan Repayment"),
    BILL_PAYMENT("Bill Payment"),
    INVESTMENT("Investment"),
    DIVIDEND("Dividend"),
    FEE_CHARGE("Fee Charge"),
    REVERSAL("Reversal");

    private final String type;

    // Constructor
    TransactionType(String type) {
        this.type = type;
    }

    // Getter for the string value
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
