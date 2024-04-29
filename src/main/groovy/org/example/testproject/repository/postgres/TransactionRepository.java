package org.example.testproject.repository.postgres;

import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.Transaction;
import org.example.testproject.domain.model.postgres.custom.IAmountCount;
import org.example.testproject.domain.model.postgres.custom.IExceededTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
            SELECT t.currencyShortname AS currency, SUM(t.amount) AS amount
              FROM Transaction t
             WHERE t.accountFrom.id = :accountId AND t.expenseCategory = :category
               AND t.datetime BETWEEN :startDate AND :expirationDate
             GROUP BY t.currencyShortname""")
    List<IAmountCount> calculateSumOfTransactions(@Param("accountId") String accountId,
                                                  @Param("category") ExpenseCategory category,
                                                  @Param("expirationDate") ZonedDateTime expirationDate,
                                                  @Param("startDate") ZonedDateTime startDate);

    @Query("""
            SELECT t AS transaction, el AS limit
              FROM Transaction t
              JOIN ExpenseLimit el ON t.accountFrom.id = el.account.id
             WHERE t.accountFrom.id = :accountId
               AND t.limitExceeded = true
               AND t.datetime BETWEEN el.limitDatetime AND el.limitExpiration
               AND el.limitDatetime = (
               SELECT MAX(el2.limitDatetime)
                 FROM ExpenseLimit el2
                WHERE el2.account.id = t.accountFrom.id
                  AND el2.limitDatetime <= t.datetime
              )""")
    List<IExceededTransaction> findAllExceededTransactions(@Param("accountId") String accountId);
}
