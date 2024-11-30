package com.app.bank.utility;

public enum DeleteAccountResponse {

    SUCCESS(200, "Account deleted successfully"),
    ACCOUNT_NOT_FOUND(404, "Account not found"),
    ACCOUNT_LOCKED(403, "Account is locked, cannot be deleted"),
    PERMISSION_DENIED(403, "You do not have permission to delete this account"),
    SERVER_ERROR(500, "An internal server error occurred while deleting the account");

    private final int code;
    private final String message;

    DeleteAccountResponse(int code, String message) {
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
