package org.example.testproject.service;

import org.example.testproject.domain.dto.ExceededTransactionDTO;
import org.example.testproject.domain.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO saveTransaction(TransactionDTO transaction);

    List<ExceededTransactionDTO> findExceededTransactions(String accountId);
}
