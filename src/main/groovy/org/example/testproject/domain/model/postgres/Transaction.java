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
@Table(name = "transactions")
public class Transaction extends AbstractEntity {
    private Account accountFrom;
    private Account accountTo;
    private Currency currencyShortname;
    private BigDecimal amount;
    private ExpenseCategory expenseCategory;
    private ZonedDateTime datetime;
    private boolean limitExceeded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from")
    public Account getAccountFrom() {
        return accountFrom;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to")
    public Account getAccountTo() {
        return accountTo;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public Currency getCurrencyShortname() {
        return currencyShortname;
    }

    @Column
    public BigDecimal getAmount() {
        return amount;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    @Column
    public ZonedDateTime getDatetime() {
        return datetime;
    }

    @Column
    public boolean isLimitExceeded() {
        return limitExceeded;
    }
}
