package com.app.bank.utility;

public enum LoginStatus {

    SUCCESS(200, "Login successful"),
    INVALID_CREDENTIALS(401, "Invalid username or password"),
    FAILURE(408,"Authentication failed, please try again later"),
    ACCOUNT_LOCKED(403, "Account is locked"),
    ACCOUNT_DISABLED(403, "Account is disabled"),
    PASSWORD_EXPIRED(403, "Password has expired"),
    USER_NOT_FOUND(404, "User not found"),
    SERVER_ERROR(500, "Internal server error"),
    TOO_MANY_ATTEMPTS(429, "Too many login attempts. Please try again later");

    private final int code;
    private final String message;

    LoginStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code + " - " + message;
    }
}

