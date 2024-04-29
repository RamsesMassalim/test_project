package org.example.testproject.mapper;

import org.example.testproject.domain.dto.TransactionDTO;
import org.example.testproject.domain.model.postgres.Transaction;
import org.example.testproject.domain.model.postgres.custom.Account;
import org.example.testproject.repository.postgres.AccountRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = IMapper.class)
public abstract class TransactionMapper implements IMapper<Transaction, TransactionDTO> {

    @Autowired
    AccountRepository accountRepository;

    @Override
    @Mappings({
            @Mapping(source = "accountFrom", target = "accountFrom", qualifiedByName = "idToAccount"),
            @Mapping(source = "accountTo", target = "accountTo", qualifiedByName = "idToAccount"),
            @Mapping(target = "limitExceeded", ignore = true)
    })
    public abstract Transaction toEntity(TransactionDTO dto);

    @Override
    @Mappings({
            @Mapping(source = "accountFrom", target = "accountFrom", qualifiedByName = "accountToId"),
            @Mapping(source = "accountTo", target = "accountTo", qualifiedByName = "accountToId")
    })
    public abstract TransactionDTO toDto(Transaction entity);


    @Named("idToAccount")
    Account idToAccount(String id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Named("accountToId")
    String accountToId(Account account) {
        return account.getId();
    }
}
