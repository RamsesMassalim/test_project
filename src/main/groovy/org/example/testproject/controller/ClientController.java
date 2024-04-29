package org.example.testproject.controller;

import org.example.testproject.domain.dto.ExceededTransactionDTO;
import org.example.testproject.domain.dto.ExpenseLimitDTO;
import org.example.testproject.service.ExpenseLimitService;
import org.example.testproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ExpenseLimitService expenseLimitService;

    @GetMapping("/transactions/exceeded_transactions")
    public ResponseEntity<List<ExceededTransactionDTO>> getExceedingTransactions(@RequestParam("accountId") String accountId) {
        List<ExceededTransactionDTO> transactions = transactionService.findExceededTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/limits/new")
    public ResponseEntity<ExpenseLimitDTO> setNewLimit(@RequestBody ExpenseLimitDTO limitDTO) {
        ExpenseLimitDTO limit = expenseLimitService.createLimit(limitDTO);

        return ResponseEntity.ok(limit);
    }

    @GetMapping("/limits/all")
    public ResponseEntity<List<ExpenseLimitDTO>> getLimits(@RequestParam("accountId") String accountId) {
        List<ExpenseLimitDTO> limits = expenseLimitService.getAllLimitsForAccount(accountId);

        return ResponseEntity.ok(limits);
    }

    @GetMapping("/limits/actual")
    public ResponseEntity<List<ExpenseLimitDTO>> getActualLimits(@RequestParam("accountId") String accountId) {
        List<ExpenseLimitDTO> limits = expenseLimitService.getActualLimitsForAccount(accountId);

        return ResponseEntity.ok(limits);
    }
}
