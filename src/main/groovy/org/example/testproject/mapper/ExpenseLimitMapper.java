package org.example.testproject.mapper;

import org.example.testproject.domain.dto.ExpenseLimitDTO;
import org.example.testproject.domain.model.postgres.ExpenseLimit;
import org.example.testproject.domain.model.postgres.custom.Account;
import org.example.testproject.repository.postgres.AccountRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = IMapper.class)
public abstract class ExpenseLimitMapper implements IMapper<ExpenseLimit, ExpenseLimitDTO> {

    @Autowired
    AccountRepository accountRepository;

    @Override
    @Mapping(source = "accountId", target = "account", qualifiedByName = "idToAccount")
    public abstract ExpenseLimit toEntity(ExpenseLimitDTO dto);

    @Override
    @Mapping(source = "account", target = "accountId", qualifiedByName = "accountToId")
    public abstract ExpenseLimitDTO toDto(ExpenseLimit entity);


    @Named("idToAccount")
    Account idToAccount(String id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Named("accountToId")
    String accountToId(Account account) {
        return account.getId();
    }

}
