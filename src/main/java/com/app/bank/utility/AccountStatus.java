package com.app.bank.utility;

public enum AccountStatus {
    ACTIVE("Account is active and in good standing."),
    INACTIVE("Account is inactive."),
    SUSPENDED("Account has been suspended due to violations."),
    CLOSED("Account has been permanently closed."),
    PENDING("Account is pending approval."),
    DEACTIVATED("Account has been voluntarily deactivated by the user.");

    private final String description;

    // Constructor to initialize the description
    AccountStatus(String description) {
        this.description = description;
    }

    // Method to get the description
    public String getDescription() {
        return description;
    }
}

