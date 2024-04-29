package org.example.testproject.domain.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.testproject.domain.enums.Currency;
import org.example.testproject.domain.enums.ExpenseCategory;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ExpenseLimitDTO extends AbstractDto {
    @Digits(integer = 10, fraction = 2)
    private BigDecimal limitSum;

    private ZonedDateTime limitDatetime;

    private ZonedDateTime limitExpiration;

    @NotNull
    private Currency limitCurrencyShortname;

    @NotNull
    private ExpenseCategory expenseCategory;

    @NotNull
    @Size(min = 10, max = 10)
    private String accountId;

    public ExpenseLimitDTO(ZonedDateTime time, ExpenseCategory category, String accountId) {
        this(new BigDecimal(1000), time, time.plusMonths(1), Currency.USD, category, accountId);
    }
}
