package org.example.testproject.schedule;

import org.example.testproject.service.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class HolidaysScheduler {
    @Autowired
    private HolidayService holidayService;

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidaysScheduler.class);

    @Scheduled(cron = "0 0 0 1 1 *")  // Fire at midnight on January 1st every year
    public void registerHolidaysEveryYear() {
        LOGGER.info("Register holidays for KZ");

        // free version is available only for previous years
        holidayService.registerHolidays("KZ", LocalDate.now().getYear() - 1);
    }
}
