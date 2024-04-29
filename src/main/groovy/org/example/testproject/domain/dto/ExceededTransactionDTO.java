package org.example.testproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceededTransactionDTO {
    private TransactionDTO transaction;
    private ExpenseLimitDTO limit;
}
