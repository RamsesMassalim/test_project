package org.example.testproject.mapper;

import org.example.testproject.domain.api.holiday.Holiday;
import org.example.testproject.domain.model.cassandra.HolidayEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface HolidayMapper {
    HolidayMapper INSTANT = Mappers.getMapper(HolidayMapper.class);

    @Mappings({
            @Mapping(source = "date", target = "month", qualifiedByName = "dateToMonth"),
            @Mapping(source = "date", target = "day", qualifiedByName = "dateToDay")
    })
    HolidayEntity toEntity(Holiday dto);

    @Named("dateToMonth")
    default int dateToMonth(LocalDate date) {
        return date.getMonthValue();
    }

    @Named("dateToDay")
    default int dateToDay(LocalDate date) {
        return date.getDayOfMonth();
    }
}
