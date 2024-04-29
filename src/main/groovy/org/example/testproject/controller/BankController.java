package org.example.testproject.controller;

import org.example.testproject.domain.dto.AccountDto;
import org.example.testproject.domain.dto.TransactionDTO;
import org.example.testproject.service.AccountService;
import org.example.testproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/transactions/new")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transaction) {
        return ResponseEntity.ok(transactionService.saveTransaction(transaction));
    }

    @PostMapping("/accounts/new")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto account) {
        return ResponseEntity.ok(accountService.saveAccount(account));
    }
}