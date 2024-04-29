package org.example.testproject.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HolidayClientConfig {

    @Bean
    public RequestInterceptor holidayApiKeyInterceptor(@Value("${holiday.api.key}") String apiKey) {
        return template -> template.query("key", apiKey);
    }
}
