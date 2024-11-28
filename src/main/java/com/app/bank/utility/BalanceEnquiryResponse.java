package com.app.bank.utility;

public enum BalanceEnquiryResponse {

    SUCCESS(200, "Balance enquiry successful."),
    ACCOUNT_NOT_FOUND(404, "Account not found."),
    UNAUTHORIZED(401, "Unauthorized to perform balance enquiry."),
    SERVER_ERROR(500, "Internal server error occurred during balance enquiry."),
    INVALID_ACCOUNT(400, "Invalid account details provided."),
    SERVICE_UNAVAILABLE(503, "Balance enquiry service is currently unavailable."),
    INVALID_ENQUIRY(504,"Insufficient balance!!!");

    private final int code;
    private final String message;

    // Constructor
    BalanceEnquiryResponse(int code, String message) {
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
