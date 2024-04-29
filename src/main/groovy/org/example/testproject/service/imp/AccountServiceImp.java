package org.example.testproject.service.imp;

import org.example.testproject.domain.dto.AccountDto;
import org.example.testproject.domain.model.postgres.custom.Account;
import org.example.testproject.mapper.AccountMapper;
import org.example.testproject.repository.postgres.AccountRepository;
import org.example.testproject.service.AccountService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper mapper;

    @Override
    public AccountDto saveAccount(AccountDto accountDto) {
        if (accountDto.getId() == null)
            throw new RuntimeException("Id is null");

        Account account = accountRepository.save(mapper.toEntity(accountDto));

        return mapper.toDto(account);
    }
}
