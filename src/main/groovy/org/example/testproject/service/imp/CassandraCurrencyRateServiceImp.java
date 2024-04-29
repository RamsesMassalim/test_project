package org.example.testproject.service.imp;

import jakarta.transaction.Transactional;
import org.example.testproject.client.CurrencyApiClient;
import org.example.testproject.domain.dto.api_response.CurrencyRateDto;
import org.example.testproject.domain.model.cassandra.CurrencyRate;
import org.example.testproject.mapper.CassandraCurrencyRateMapper;
import org.example.testproject.repository.cassandra.CassandraCurrencyRateRepository;
import org.example.testproject.service.CurrencyRateService;
import org.example.testproject.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CassandraCurrencyRateServiceImp implements CurrencyRateService {
    @Autowired
    private CassandraCurrencyRateRepository cassandraCurrencyRateRepository;

    @Autowired
    private CurrencyApiClient currencyApiClient;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private CassandraCurrencyRateMapper mapper;

    @Override
    @Transactional
    public void updateDailyRates(String currencyPair) {
        if (isHoliday())
            return;

        closeAllNonClosedRates(currencyPair);
        addNewDailyRate(currencyPair);
    }

    @Override
    public BigDecimal getActualRate(String currencyPair) {
        return cassandraCurrencyRateRepository.findBySymbolAndTimeStamp(currencyPair)
                .map(CurrencyRate::getRate)
                .orElseGet(() -> getPreviousCloseRate(currencyPair));
    }

    @Override
    public BigDecimal getPreviousCloseRate(String currencyPair) {
        return cassandraCurrencyRateRepository.findFirstBySymbolAndCloseIsFalseOrderByTimeStampDesc(currencyPair)
                .map(CurrencyRate::getRate)
                .orElseThrow(() -> new RuntimeException("Rate not available " + currencyPair));
    }


    private boolean isHoliday() {
        return holidayService.isWeekDay("KZ", LocalDate.now());
    }

    private void addNewDailyRate(String currencyPair) {
        CurrencyRateDto response = currencyApiClient.getDailyRates(currencyPair);

        CurrencyRate rate = mapper.toEntity(response);

        cassandraCurrencyRateRepository.save(rate);
    }

    private void closeAllNonClosedRates(String symbol) {
        List<CurrencyRate> unclosedCurrencyRates = cassandraCurrencyRateRepository.findAllBySymbolAndCloseIsFalse(symbol);

        unclosedCurrencyRates.forEach(cr ->
                cassandraCurrencyRateRepository.updateCurrencyRatesBySymbol(cr.getSymbol(), cr.getTimeStamp()));
    }
}
