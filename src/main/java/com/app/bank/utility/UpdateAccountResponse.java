package com.app.bank.utility;

public enum UpdateAccountResponse {

    SUCCESS(200, "Account updated successfully"),
    ACCOUNT_NOT_FOUND(404, "Account not found"),
    SUCCESS_PASSWORD(200,"Password updated successfully"),
    FAILURE_PASSWORD(404,"Password update failed, please try again later"),
    PASSWORD_MISMATCH(401,"Incorrect old password"),
    INVALID_DATA(400, "Invalid account data provided"),
    ACCOUNT_LOCKED(403, "Account is locked, cannot be updated"),
    PERMISSION_DENIED(403, "You do not have permission to update this account"),
    SERVER_ERROR(500, "An internal server error occurred while updating the account");

    private final int code;
    private final String message;

    UpdateAccountResponse(int code, String message) {
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