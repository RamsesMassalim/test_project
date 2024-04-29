package org.example.testproject.service;

import org.example.testproject.domain.dto.ExpenseLimitDTO;
import org.example.testproject.domain.enums.ExpenseCategory;

import java.util.List;

public interface ExpenseLimitService {
    ExpenseLimitDTO createLimit(ExpenseLimitDTO expenseLimitDTO);

    List<ExpenseLimitDTO> getAllLimitsForAccount(String accountId);

    List<ExpenseLimitDTO> getActualLimitsForAccount(String accountId);

    ExpenseLimitDTO getLimitForCategoryOrCreateNew(String accountId, ExpenseCategory expenseCategory);
}
