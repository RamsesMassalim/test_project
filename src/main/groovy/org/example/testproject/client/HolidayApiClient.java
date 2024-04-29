package org.example.testproject.client;

import org.example.testproject.config.HolidayClientConfig;
import org.example.testproject.domain.api.holiday.HolidayResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "holiday-api", url = "https://holidayapi.com/v1", configuration = HolidayClientConfig.class)
public interface HolidayApiClient {

    @GetMapping("/holidays")
    HolidayResponse getHolidays(@RequestParam("country") String country,
                                @RequestParam("year") int year,
                                @RequestParam("public") boolean isPublic);
}
