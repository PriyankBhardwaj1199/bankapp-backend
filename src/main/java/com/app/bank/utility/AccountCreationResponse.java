package com.app.bank.utility;

public enum AccountCreationResponse {
    SUCCESS(200, "Account created successfully."),
    INVALID_INPUT(400, "Invalid input provided."),
    DUPLICATE_ACCOUNT(409, "An account with the same details already exists."),
    SERVER_ERROR(500, "Internal server error occurred during account creation."),
    INSUFFICIENT_DATA(422, "Insufficient data provided for account creation."),
    UNAUTHORIZED(401, "Unauthorized to create an account.");

    private final int code;
    private final String message;

    // Constructor to initialize code and message
    AccountCreationResponse(int code, String message) {
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

