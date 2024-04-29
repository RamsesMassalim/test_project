package org.example.testproject.client;

import org.example.testproject.config.CurrencyClientConfig;
import org.example.testproject.domain.dto.api_response.CurrencyRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currency-api", url = "https://api.twelvedata.com", configuration = CurrencyClientConfig.class)
public interface CurrencyApiClient {

    @GetMapping("/exchange_rate")
    CurrencyRateDto getDailyRates(@RequestParam("symbol") String symbol);
}
