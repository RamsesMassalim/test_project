package org.example.testproject;

import org.example.testproject.domain.dto.AccountDto;
import org.example.testproject.domain.dto.ExpenseLimitDTO;
import org.example.testproject.domain.dto.TransactionDTO;
import org.example.testproject.domain.enums.Currency;
import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.ExpenseLimit;
import org.example.testproject.domain.model.postgres.Transaction;
import org.example.testproject.domain.model.postgres.custom.Account;
import org.example.testproject.mapper.AccountMapper;
import org.example.testproject.mapper.ExpenseLimitMapper;
import org.example.testproject.mapper.TransactionMapper;
import org.example.testproject.repository.postgres.AccountRepository;
import org.example.testproject.repository.postgres.ExpenseLimitRepository;
import org.example.testproject.repository.postgres.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
public abstract class AbstractServiceTest {

    @Autowired
    protected TransactionMapper transactionMapper;

    @Autowired
    protected ExpenseLimitMapper limitMapper;

    @Autowired
    protected AccountMapper accountMapper;

    @Autowired
    protected TransactionRepository transactionRepository;

    @Autowired
    protected ExpenseLimitRepository limitRepository;

    @Autowired
    protected AccountRepository accountRepository;

    protected static String createId(String prefix, String root, String suffix) {
        return "test" + prefix + root + suffix;
    }

    protected AccountDto createAccountDto(String prefix, String root, String suffix) {
        String id = "test" + prefix + root + suffix;

        return createAccountDto(id);
    }

    protected AccountDto createAccountDto(String id) {
        return new AccountDto(id);
    }

    protected ExpenseLimitDTO createLimitDto(String prefix, String suffix) {
        return createLimitDto(prefix, suffix,
                BigDecimal.valueOf(1000),
                Currency.USD,
                ExpenseCategory.SERVICES);
    }

    protected ExpenseLimitDTO createLimitDto(String prefix, String suffix, BigDecimal amount, Currency currency, ExpenseCategory category) {
        return createLimitDto(prefix, suffix,
                amount,
                currency,
                category,
                ZonedDateTime.now(),
                ZonedDateTime.now().plusMonths(1));
    }

    protected ExpenseLimitDTO createLimitDto(String prefix, String suffix, BigDecimal amount, Currency currency, ExpenseCategory category, ZonedDateTime start, ZonedDateTime exp) {
        String id = createId(prefix, "00", suffix);

        return new ExpenseLimitDTO(amount,
                start,
                exp,
                currency,
                category,
                id);
    }

    protected ExpenseLimitDTO createLimitDto(String id, BigDecimal amount, Currency currency, ExpenseCategory category, ZonedDateTime start, ZonedDateTime exp) {
        return new ExpenseLimitDTO(amount,
                start,
                exp,
                currency,
                category,
                id);
    }

    protected ExpenseLimitDTO saveLimitDto(ExpenseLimitDTO limit) {
        ExpenseLimit entity = limitMapper.toEntity(limit);

        entity.setAccount(new Account(limit.getAccountId()));
        limit.setLimitDatetime(ZonedDateTime.now());

        return limitMapper.toDto(saveLimit(entity));
    }

    protected ExpenseLimitDTO createAndSaveLimitDto(String prefix, String suffix) {
        return saveLimitDto(createLimitDto(prefix, suffix));
    }

    protected ExpenseLimitDTO createAndSaveLimitDto(String prefix, String suffix, BigDecimal amount, Currency currency, ExpenseCategory category) {
        return saveLimitDto(createLimitDto(prefix, suffix, amount, currency, category));
    }

    protected ExpenseLimitDTO createAndSaveLimitDto(String prefix, String suffix, BigDecimal amount, Currency currency, ExpenseCategory category, ZonedDateTime start, ZonedDateTime exp) {
        return saveLimitDto(createLimitDto(prefix, suffix, amount, currency, category, start, exp));
    }

    protected ExpenseLimitDTO createAndSaveLimitDto(String id, BigDecimal amount, Currency currency, ExpenseCategory category, ZonedDateTime start, ZonedDateTime exp) {
        return saveLimitDto(createLimitDto(id, amount, currency, category, start, exp));
    }

    protected TransactionDTO createTransactionDTO(String prefix, String suffix) {
        String accountFrom = createId(prefix, "fr", suffix);
        String accountTo = createId(prefix, "to", suffix);

        return new TransactionDTO(accountFrom,
                accountTo,
                Currency.USD,
                BigDecimal.valueOf(100),
                ExpenseCategory.SERVICES,
                ZonedDateTime.now());
    }

    protected TransactionDTO createTransactionDTO(String prefix, String suffix, Currency currency, BigDecimal amount, ExpenseCategory category) {
        String accountFrom = createId(prefix, "fr", suffix);
        String accountTo = createId(prefix, "to", suffix);

        return new TransactionDTO(accountFrom, accountTo, currency, amount, category, ZonedDateTime.now());
    }

    protected TransactionDTO saveTransactionDTO(TransactionDTO transaction) {
        return saveTransactionDTO(transaction, false);
    }

    protected TransactionDTO saveTransactionDTO(TransactionDTO transaction, boolean isExceeded) {
        Transaction entity = transactionMapper.toEntity(transaction);

        entity.setAccountFrom(new Account(transaction.getAccountFrom()));
        entity.setAccountTo(new Account(transaction.getAccountTo()));
        entity.setLimitExceeded(isExceeded);

        return transactionMapper.toDto(saveTransaction(entity));
    }

    protected TransactionDTO createAndSaveTransactionDTO(String prefix, String suffix) {
        return saveTransactionDTO(createTransactionDTO(prefix, suffix));
    }

    protected TransactionDTO createAndSaveTransactionDTO(String prefix, String suffix, Currency currency, BigDecimal amount, ExpenseCategory category) {
        return saveTransactionDTO(createTransactionDTO(prefix, suffix, currency, amount, category));
    }

    protected TransactionDTO createAndSaveTransactionDTO(String prefix, String suffix, Currency currency, BigDecimal amount, ExpenseCategory category, boolean isExceeded) {
        return saveTransactionDTO(createTransactionDTO(prefix, suffix, currency, amount, category), isExceeded);
    }

    protected Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    private ExpenseLimit saveLimit(ExpenseLimit limit) {
        saveAccount(limit.getAccount());

        return limitRepository.save(limit);
    }

    private Transaction saveTransaction(Transaction transaction) {
        saveAccount(transaction.getAccountFrom());
        saveAccount(transaction.getAccountTo());

        return transactionRepository.save(transaction);
    }

    protected static void assertLimit(ExpenseLimitDTO actual, ExpenseLimitDTO expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getLimitDatetime()).isNotNull();
        assertThat(actual.getLimitExpiration()).isNotNull();
        assertThat(actual.getLimitSum()).isEqualTo(expected.getLimitSum());
        assertThat(actual.getLimitCurrencyShortname()).isEqualTo(expected.getLimitCurrencyShortname());
        assertThat(actual.getExpenseCategory()).isEqualTo(expected.getExpenseCategory());
        assertThat(actual.getAccountId()).isEqualTo(expected.getAccountId());
    }

    protected static void assertTransaction(TransactionDTO actual, TransactionDTO expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getExpenseCategory()).isEqualTo(expected.getExpenseCategory());
        assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        assertThat(actual.getCurrencyShortname()).isEqualTo(expected.getCurrencyShortname());
        assertThat(actual.getAccountFrom()).isEqualTo(expected.getAccountFrom());
        assertThat(actual.getAccountTo()).isEqualTo(expected.getAccountTo());
    }
}
