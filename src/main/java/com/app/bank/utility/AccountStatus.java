package com.app.bank.utility;

public enum AccountStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    SUSPENDED("SUSPENDED"),
    CLOSED("CLOSED"),
    PENDING("PENDING"),
    DEACTIVATED("DEACTIVATED");

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

