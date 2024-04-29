package org.example.testproject.domain.enums;

public enum Currency {
    USD, RUB, KZT;

    public static String getCurrencyPair(Currency from, Currency to) {
        return from + "/" + to;
    }
}
