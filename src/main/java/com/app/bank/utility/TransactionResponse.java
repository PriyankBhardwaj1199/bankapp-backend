package com.app.bank.utility;

public enum TransactionResponse {
    SUCCESS(200, "COMPLETED"),
    PENDING(202, "PENDING"),
    FAILED(400, "FAILED"),
    DELETED(200, "Transaction deleted successfully."),
    TRANSACTION_NOT_FOUND(404, "Transaction not found"),
    INSUFFICIENT_FUNDS(402, "Transaction failed due to insufficient funds."),
    UNAUTHORIZED(401, "Unauthorized transaction."),
    ACCOUNT_NOT_FOUND(404, "Account not found."),
    SERVER_ERROR(500, "Internal server error occurred during the transaction."),
    DUPLICATE_REQUEST(409, "Duplicate transaction request detected."),
    LIMIT_EXCEEDED(403, "Transaction limit exceeded."),
    SERVICE_UNAVAILABLE(503, "Transaction service is temporarily unavailable.");

    private final int code;
    private final String message;

    // Constructor
    TransactionResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getter for code
    public int getCode() {
        return code;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }
}
