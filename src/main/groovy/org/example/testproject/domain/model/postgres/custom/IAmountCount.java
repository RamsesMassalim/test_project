package org.example.testproject.domain.model.postgres.custom;

import org.example.testproject.domain.enums.Currency;

import java.math.BigDecimal;

public interface IAmountCount {
    Currency getCurrency();
    BigDecimal getAmount();
}
