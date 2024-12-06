package com.app.bank.utility;

public enum CardsResponse {
    // Success responses
    CARD_APPLIED_SUCCESSFULLY(200,"Application for card successful. Your card will be approved soon."),
    CARD_ISSUED_SUCCESSFULLY(200, "Card issued successfully."),
    CARD_ACTIVATED_SUCCESSFULLY(200, "Card activated successfully."),
    CARD_BLOCKED_SUCCESSFULLY(200, "Card blocked successfully."),
    CARD_DEACTIVATED_SUCCESSFULLY(200, "Card deactivated successfully."),
    CARD_PENDING_APPROVAL(200,"Cannot perform the required action, card must be activated first."),
    CARD_BLOCKED(200,"Cannot perform the required action, card is blocked."),
    CARD_EXPIRED(200,"Cannot perform the required action, card is expired."),
    CARD_INACTIVE(200,"Cannot perform the required action, card is inactive."),
    PIN_GENERATED(200,"Pin generated successfully."),

    // Client error responses
    INVALID_CARD_DETAILS(400, "Invalid card details provided."),
    INSUFFICIENT_BALANCE(400, "Insufficient balance for card operation."),
    ACCOUNT_NOT_FOUND(404, "Associated account not found."),
    CARD_NOT_FOUND(404, "Card not found."),
    CARD_ALREADY_ACTIVE(409, "Card is already active."),
    CARD_ALREADY_BLOCKED(409, "Card is already blocked."),
    CARD_ALREADY_EXISTS(409, "The requested card already exists with your account."),

    // Server error responses
    INTERNAL_SERVER_ERROR(500, "An internal error occurred while processing the card request."),
    SERVICE_UNAVAILABLE(503, "Card service is currently unavailable.");

    private final int code;
    private final String message;

    CardsResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
