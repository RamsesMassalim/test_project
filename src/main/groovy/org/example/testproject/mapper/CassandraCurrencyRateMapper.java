package org.example.testproject.mapper;

import org.example.testproject.domain.dto.api_response.CurrencyRateDto;
import org.example.testproject.domain.model.cassandra.CurrencyRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface CassandraCurrencyRateMapper {
    CassandraCurrencyRateMapper INSTANT = Mappers.getMapper(CassandraCurrencyRateMapper.class);


    @Mappings({
            @Mapping(source = "timestamp", target = "timeStamp", qualifiedByName = "unixToInstant"),
            @Mapping(target = "close", ignore = true)
    })
    CurrencyRate toEntity(CurrencyRateDto dto);

    @Named("unixToInstant")
    default Instant unixToInstant(Long unixTimestamp) {
        return unixTimestamp == null ? null :
                Instant.ofEpochSecond(unixTimestamp);
    }
}
