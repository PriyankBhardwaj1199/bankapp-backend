package com.app.bank.utility;

public enum CardType {

    CREDIT("Credit",null),
    DEBIT("Debit",null),
    VISA("Visa", "4"),
    MASTERCARD("MasterCard", "5"),
    RUPAYCARD("RuPay", "60"),

    AMERICAN_EXPRESS("American Express", "37"),
    DISCOVER("Discover", "6011"),
    JCB("JCB", "35"),
    DINERS_CLUB("Diners Club", "300");

    private final String name;
    private final String prefix;

    CardType(String name, String prefixes) {
        this.name = name;
        this.prefix = prefixes;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

}

