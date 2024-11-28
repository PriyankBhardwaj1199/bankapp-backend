package com.app.bank.utility;

public enum TransferResponse {
    SUCCESS(200, "Amount transferred successfully."),
    INSUFFICIENT_BALANCE(402, "Insufficient balance for the transfer."),
    SOURCE_ACCOUNT_NOT_FOUND(404, "Sender account not found."),
    DESTINATION_ACCOUNT_NOT_FOUND(405, "Recipient account not found."),
    UNAUTHORIZED(401, "Unauthorized to perform the transfer."),
    INVALID_INPUT(400, "Invalid input details provided for the transfer."),
    SERVER_ERROR(500, "Internal server error occurred during the transfer."),
    LIMIT_EXCEEDED(403, "Transfer limit exceeded."),
    DUPLICATE_TRANSFER(409, "Duplicate transfer request detected."),
    SERVICE_UNAVAILABLE(503, "Transfer service is currently unavailable."),
    TRANSACTION_PENDING(202, "Transfer request is pending processing.");

    private final int code;
    private final String message;

    // Constructor
    TransferResponse(int code, String message) {
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
