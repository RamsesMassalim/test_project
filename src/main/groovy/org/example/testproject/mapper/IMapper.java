package org.example.testproject.mapper;

import org.example.testproject.domain.dto.AbstractDto;
import org.example.testproject.domain.model.postgres.AbstractEntity;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValuePropertyMappingStrategy;

@MapperConfig(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IMapper<E extends AbstractEntity, D extends AbstractDto> {
    D toDto(E entity);

    E toEntity(D dto);
}
