package org.example.testproject.domain.model.postgres;

import jakarta.persistence.*;
import lombok.*;
import org.example.testproject.domain.enums.Currency;
import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.custom.Account;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_limits")
public class ExpenseLimit extends AbstractEntity {
    private BigDecimal limitSum;
    private ZonedDateTime limitDatetime;
    private ZonedDateTime limitExpiration;
    private Currency limitCurrencyShortname;
    private ExpenseCategory expenseCategory;
    private Account account;

    @Column(name = "limit_sum", nullable = false, precision = 19, scale = 2)
    public BigDecimal getLimitSum() {
        return limitSum;
    }

    @Column
    public ZonedDateTime getLimitDatetime() {
        return limitDatetime;
    }

    @Column
    public ZonedDateTime getLimitExpiration() {
        return limitExpiration;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public Currency getLimitCurrencyShortname() {
        return limitCurrencyShortname;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    public Account getAccount() {
        return account;
    }
}
