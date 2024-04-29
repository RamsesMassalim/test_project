package org.example.testproject.domain.model.postgres.custom;

import org.example.testproject.domain.model.postgres.ExpenseLimit;
import org.example.testproject.domain.model.postgres.Transaction;

public interface IExceededTransaction {
    Transaction getTransaction();
    ExpenseLimit getLimit();
}
