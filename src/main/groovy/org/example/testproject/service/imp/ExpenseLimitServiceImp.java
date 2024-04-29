package org.example.testproject.service.imp;

import org.example.testproject.domain.dto.ExpenseLimitDTO;
import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.ExpenseLimit;
import org.example.testproject.mapper.ExpenseLimitMapper;
import org.example.testproject.repository.postgres.ExpenseLimitRepository;
import org.example.testproject.service.ExpenseLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ExpenseLimitServiceImp implements ExpenseLimitService {
    @Autowired
    private ExpenseLimitRepository expenseLimitRepository;

    @Autowired
    private ExpenseLimitMapper mapper;

    @Override
    public ExpenseLimitDTO createLimit(ExpenseLimitDTO limitDTO) {
        if (limitDTO.getId() != null)
            throw new RuntimeException("ID must be null");

        ZonedDateTime today = ZonedDateTime.now();

        limitDTO.setLimitDatetime(today);

        expenseLimitRepository.findActualLimitForCategory(limitDTO.getAccountId(), limitDTO.getExpenseCategory())
                .ifPresentOrElse(
                        (l) -> limitDTO.setLimitExpiration(l.getLimitExpiration()),
                        () -> limitDTO.setLimitExpiration(today.plusMonths(1))
                );

        ExpenseLimit limit = mapper.toEntity(limitDTO);

        ExpenseLimit saved = expenseLimitRepository.save(limit);

        return mapper.toDto(saved);
    }

    @Override
    public List<ExpenseLimitDTO> getAllLimitsForAccount(String accountId) {
        List<ExpenseLimit> limits = expenseLimitRepository.findAllByAccountId(accountId);

        return limits.stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @Override
    public List<ExpenseLimitDTO> getActualLimitsForAccount(String accountId) {
        List<ExpenseLimit> limits = expenseLimitRepository.findExpenseLimitsByAccountId(accountId);

        return limits.stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @Override
    public ExpenseLimitDTO getLimitForCategoryOrCreateNew(String accountId, ExpenseCategory expenseCategory) {
        ExpenseLimitDTO limit = expenseLimitRepository.findActualLimitForCategory(accountId, expenseCategory)
                .map(mapper::toDto)
                .orElseGet(() -> createLimit(new ExpenseLimitDTO(ZonedDateTime.now(), expenseCategory, accountId)));

        return limit;
    }
}
