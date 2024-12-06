package com.app.bank.utility;

public enum CardState {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BLOCKED("BLOCKED"),
    EXPIRED("EXPIRED"),
    PENDING_ACTIVATION("REQUESTED"),
    REVOKED("REVOKED"),
    PIN_GENERATED("GENERATED"),
    PIN_PENDING("PENDING");

    private final String description;

    CardState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

