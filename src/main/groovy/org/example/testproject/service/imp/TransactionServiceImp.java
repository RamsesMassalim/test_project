package org.example.testproject.service.imp;

import org.example.testproject.domain.dto.ExceededTransactionDTO;
import org.example.testproject.domain.dto.ExpenseLimitDTO;
import org.example.testproject.domain.dto.TransactionDTO;
import org.example.testproject.domain.enums.Currency;
import org.example.testproject.domain.model.postgres.Transaction;
import org.example.testproject.domain.model.postgres.custom.IAmountCount;
import org.example.testproject.domain.model.postgres.custom.IExceededTransaction;
import org.example.testproject.mapper.ExpenseLimitMapper;
import org.example.testproject.mapper.TransactionMapper;
import org.example.testproject.repository.postgres.TransactionRepository;
import org.example.testproject.service.CurrencyRateService;
import org.example.testproject.service.ExpenseLimitService;
import org.example.testproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TransactionServiceImp implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ExpenseLimitService expenseLimitService;

    @Autowired
    private CurrencyRateService currencyRateService;

    @Autowired
    private TransactionMapper mapper;

    @Autowired
    private ExpenseLimitMapper limitMapper;

    @Override
    public TransactionDTO saveTransaction(TransactionDTO transaction) {
        // Проверка лимита для категории расходов
        ExpenseLimitDTO limit = expenseLimitService.getLimitForCategoryOrCreateNew(transaction.getAccountFrom(), transaction.getExpenseCategory());

        BigDecimal limitReminder = calculateLimitReminder(limit, transaction.getCurrencyShortname());

        boolean isExceeded = transaction.getAmount()
                .compareTo(limitReminder) > 0;

        Transaction newTransaction = mapper.toEntity(transaction);

        newTransaction.setLimitExceeded(isExceeded);
        newTransaction.setDatetime(ZonedDateTime.now());

        return mapper.toDto(transactionRepository.save(newTransaction));
    }

    @Override
    public List<ExceededTransactionDTO> findExceededTransactions(String accountId) {

        List<IExceededTransaction> transactions = transactionRepository.findAllExceededTransactions(accountId);

        return transactions.stream()
                .map(x -> new ExceededTransactionDTO(mapper.toDto(x.getTransaction()), limitMapper.toDto(x.getLimit())))
                .collect(toList());
    }

    private BigDecimal calculateLimitReminder(ExpenseLimitDTO limit, Currency transactionCurrency) {
        ZonedDateTime startDate = limit.getLimitExpiration().minusMonths(1);

        List<IAmountCount> amountSpent = transactionRepository.calculateSumOfTransactions(limit.getAccountId(), limit.getExpenseCategory(), limit.getLimitExpiration(), startDate);

        BigDecimal amount = amountSpent.stream()
                .map(e -> convertCurrency(e.getAmount(), e.getCurrency(), transactionCurrency))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return convertCurrency(limit.getLimitSum(), limit.getLimitCurrencyShortname(), transactionCurrency)
                .subtract(amount);
    }

    private BigDecimal convertCurrency(BigDecimal amount, Currency from, Currency to) {
        if (from == to)
            return amount;

        BigDecimal rate = currencyRateService.getActualRate(from + "/" + to);

        return amount.multiply(rate);
    }
}
