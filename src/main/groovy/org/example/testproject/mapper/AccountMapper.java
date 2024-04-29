package org.example.testproject.mapper;

import org.example.testproject.domain.dto.AccountDto;
import org.example.testproject.domain.model.postgres.custom.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toDto(Account entity);

    Account toEntity(AccountDto dto);
}
