package com.app.bank.utility;

public enum Roles {

    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    // Constructor
    Roles(String role) {
        this.role = role;
    }

    // Getter for the string value
    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }
}
