package org.example.testproject.repository.cassandra;

import org.example.testproject.domain.model.cassandra.HolidayEntity;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CassandraHolidaysRepository extends CassandraRepository<HolidayEntity, String> {
    @AllowFiltering
    Optional<HolidayEntity> findHolidayEntityByCountryAndMonthAndDay(String country, int month, int day);
}
