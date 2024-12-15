package com.app.bank.utility;

public enum BankStatementResponse {

    // Success responses
    STATEMENT_GENERATED_SUCCESSFULLY(200, "The bank statement has been generated and sent to you via email successfully."),
    EMAIL_SENT_SUCCESSFULLY(200, "The bank statement email has been sent successfully."),
    STATEMENT_DOWNLOADED_SUCCESSFULLY(200, "The bank statement has been downloaded successfully."),
    STATEMENT_NOT_FOUND(200, "The intended bank statement is not found for this account."),
    STATEMENT_DELETED_SUCCESSFULLY(200, "The bank statement has been deleted successfully."),

    // Client-side error responses
    STATEMENT_GENERATION_FAILED(400, "Failed to generate the bank statement."),
    EMAIL_SENDING_FAILED(400, "Failed to send the bank statement email."),
    DOWNLOAD_FAILED(400, "Failed to download the bank statement."),
    INVALID_ACCOUNT_DETAILS(400, "The provided account details are invalid."),
    NO_TRANSACTIONS_FOUND(404, "No transactions found for the specified period."),

    // Validation responses
    MISSING_ACCOUNT_DETAILS(422, "Account details are missing."),
    MISSING_DATE_RANGE(422, "Date range for the statement is missing."),
    INVALID_DATE_RANGE(422, "The specified date range is invalid."),

    // General error responses
    STATEMENT_REQUEST_IN_PROGRESS(202, "Your request to generate the statement is being processed."),
    SERVICE_UNAVAILABLE(503, "The bank statement service is temporarily unavailable. Please try again later.");

    private final int code;
    private final String message;

    BankStatementResponse(int code, String message) {
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

