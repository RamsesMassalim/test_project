package org.example.testproject.service;

import java.time.LocalDate;

public interface HolidayService {
    void registerHolidays(String country, int year);

    boolean isWeekDay(String country, LocalDate date);
}
