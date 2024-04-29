package org.example.testproject.repository.postgres;

import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.ExpenseLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseLimitRepository extends JpaRepository<ExpenseLimit, Long> {
    @Query(value = "WITH cte AS (\n" +
            "    SELECT *,\n" +
            "           ROW_NUMBER() OVER (PARTITION BY el.expense_category ORDER BY el.limit_datetime DESC) AS rn\n" +
            "      FROM expense_limits el\n" +
            "     WHERE el.account_id = :accountId AND el.limit_expiration >= CURRENT_DATE\n" +
            ")\n" +
            "SELECT *\n" +
            "  FROM cte el2\n" +
            " WHERE el2.rn = 1", nativeQuery = true)
    List<ExpenseLimit> findExpenseLimitsByAccountId(@Param("accountId") String accountId);

    List<ExpenseLimit> findAllByAccountId(String accountId);

    @Query("SELECT el\n" +
            "  FROM ExpenseLimit el\n" +
            " WHERE el.account.id = :accountId AND el.expenseCategory = :category AND el.limitExpiration >= CURRENT DATE\n" +
            " ORDER BY el.limitDatetime DESC\n" +
            " LIMIT 1")
    Optional<ExpenseLimit> findActualLimitForCategory(@Param("accountId") String accountId,
                                                      @Param("category") ExpenseCategory category);
}
