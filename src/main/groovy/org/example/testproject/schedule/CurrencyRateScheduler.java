package org.example.testproject.schedule;

import org.example.testproject.service.CurrencyRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRateScheduler {
    @Autowired
    private CurrencyRateService currencyRateService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyRateScheduler.class);

    @Scheduled(cron = "0 0 1 * * ?")  // Запуск каждый день в 1:00 ночи
    public void updateCurrencyRates() {
        LOGGER.info("Update currency rates");

        currencyRateService.updateDailyRates("KZT/USD");
        currencyRateService.updateDailyRates("RUB/USD");
    }
}
