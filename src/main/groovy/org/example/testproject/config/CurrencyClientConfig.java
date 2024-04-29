package org.example.testproject.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyClientConfig {

    @Bean
    public RequestInterceptor currencyApiKeyInterceptor(@Value("${currency.api.key}") String apiKey) {
        return template -> template.query("apikey", apiKey);
    }
}
