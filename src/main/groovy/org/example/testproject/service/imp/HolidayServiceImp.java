package org.example.testproject.service.imp;

import org.example.testproject.client.HolidayApiClient;
import org.example.testproject.domain.api.holiday.HolidayResponse;
import org.example.testproject.domain.model.cassandra.HolidayEntity;
import org.example.testproject.mapper.HolidayMapper;
import org.example.testproject.repository.cassandra.CassandraHolidaysRepository;
import org.example.testproject.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class HolidayServiceImp implements HolidayService {

    @Autowired
    private CassandraHolidaysRepository holidaysRepository;

    @Autowired
    private HolidayApiClient holidayApiClient;

    @Autowired
    private HolidayMapper mapper;

    @Override
    public void registerHolidays(String country, int year) {
        HolidayResponse holidays = holidayApiClient.getHolidays(country, year, true);

        holidays.getHolidays().stream()
                .map(mapper::toEntity)
                .forEach(holidaysRepository::save);
    }

    @Override
    public boolean isWeekDay(String country, LocalDate date) {
        return isWeekend(date) || isHoliday(country, date);
    }

    private static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isHoliday(String country, LocalDate date) {
        Optional<HolidayEntity> holidayOpt = holidaysRepository.findHolidayEntityByCountryAndMonthAndDay(country,
                date.getMonthValue(),
                date.getDayOfMonth());

        return holidayOpt.isPresent();
    }
}
