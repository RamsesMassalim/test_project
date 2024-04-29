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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TransactionDTO extends AbstractDto {

    @NotNull
    @Size(min = 10, max = 10)
    private String accountFrom;

    @NotNull
    @Size(min = 10, max = 10)
    private String accountTo;

    @NotNull
    private Currency currencyShortname;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;

    @NotNull
    private ExpenseCategory expenseCategory;

    private ZonedDateTime datetime;
}
