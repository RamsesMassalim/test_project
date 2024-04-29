package org.example.testproject.service;

import java.math.BigDecimal;

public interface CurrencyRateService {
    void updateDailyRates(String currencyPair);

    BigDecimal getActualRate(String currencyPair);

    BigDecimal getPreviousCloseRate(String currencyPair);
}
