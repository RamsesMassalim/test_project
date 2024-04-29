package org.example.testproject;

import org.example.testproject.domain.enums.Currency;
import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.ExpenseLimit;
import org.example.testproject.domain.model.postgres.Transaction;
import org.example.testproject.domain.model.postgres.custom.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public abstract class AbstractJpaTest {

    @Autowired
    protected TestEntityManager entityManager;

    protected Account saveAccount(String prefix, String root, String suffix) {
        String id = "test" + prefix + root + suffix;

        return saveAccount(id);
    }

    protected Account saveAccount(String id) {
        Account account = new Account(id);

        if (entityManager.find(Account.class, id) != null) {
            return account;
        }

        return entityManager.persistAndFlush(account);
    }

    protected ExpenseLimit createLimit(String prefix, String suffix) {
        return createLimit(prefix, suffix,
                BigDecimal.valueOf(1000),
                Currency.USD,
                ExpenseCategory.SERVICES);
    }

    protected ExpenseLimit createLimit(String prefix, String suffix, BigDecimal amount, Currency currency, ExpenseCategory category) {
        return createLimit(prefix, suffix,
                amount,
                currency,
                category,
                ZonedDateTime.now(),
                ZonedDateTime.now().plusMonths(1));
    }

    protected ExpenseLimit createLimit(String prefix, String suffix, BigDecimal amount, Currency currency, ExpenseCategory category, ZonedDateTime start, ZonedDateTime exp) {
        Account account = saveAccount(prefix, "00", suffix);

        return new ExpenseLimit(amount,
                start,
                exp,
                currency,
                category,
                account);
    }

    protected ExpenseLimit createLimit(String id, BigDecimal amount, Currency currency, ExpenseCategory category, ZonedDateTime start, ZonedDateTime exp) {
        Account account = saveAccount(id);

        return new ExpenseLimit(amount,
                start,
                exp,
                currency,
                category,
                account);
    }


    protected ExpenseLimit saveLimit(ExpenseLimit limit) {
        return entityManager.persistAndFlush(limit);
    }

    protected ExpenseLimit createAndSaveLimit(String prefix, String suffix) {
        return saveLimit(createLimit(prefix, suffix));
    }

    protected ExpenseLimit createAndSaveLimit(String prefix, String suffix, BigDecimal amount, Currency currency, ExpenseCategory category) {
        return saveLimit(createLimit(prefix, suffix, amount, currency, category));
    }

    protected ExpenseLimit createAndSaveLimit(String prefix, String suffix, BigDecimal amount, Currency currency, ExpenseCategory category, ZonedDateTime start, ZonedDateTime exp) {
        return saveLimit(createLimit(prefix, suffix, amount, currency, category, start, exp));
    }

    protected ExpenseLimit createAndSaveLimit(String id, BigDecimal amount, Currency currency, ExpenseCategory category, ZonedDateTime start, ZonedDateTime exp) {
        return saveLimit(createLimit(id, amount, currency, category, start, exp));
    }

    protected Transaction createTransaction(String prefix, String suffix) {
        Account accountFrom = saveAccount(prefix, "fr", suffix);
        Account accountTo = saveAccount(prefix, "to", suffix);

        return new Transaction(accountFrom,
                accountTo,
                Currency.USD,
                BigDecimal.valueOf(100),
                ExpenseCategory.SERVICES,
                ZonedDateTime.now(),
                false);
    }

    protected Transaction createTransaction(String prefix, String suffix, Currency currency, BigDecimal amount, ExpenseCategory category, boolean isExceeded) {
        Account accountFrom = saveAccount(prefix, "fr", suffix);
        Account accountTo = saveAccount(prefix, "to", suffix);

        return new Transaction(accountFrom, accountTo, currency, amount, category, ZonedDateTime.now(), isExceeded);
    }

    protected Transaction saveTransaction(Transaction transaction) {
        return entityManager.persistAndFlush(transaction);
    }

    protected Transaction createAndSaveTransaction(String prefix, String suffix) {
        return saveTransaction(createTransaction(prefix, suffix));
    }

    protected Transaction createAndSaveTransaction(String prefix, String suffix, Currency currency, BigDecimal amount, ExpenseCategory category, boolean isExceeded) {
        return saveTransaction(createTransaction(prefix, suffix, currency, amount, category, isExceeded));
    }

    protected static void assertLimit(ExpenseLimit actual, ExpenseLimit expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getLimitDatetime()).isEqualTo(expected.getLimitDatetime());
        assertThat(actual.getLimitExpiration()).isEqualTo(expected.getLimitExpiration());
        assertThat(actual.getLimitSum()).isEqualTo(expected.getLimitSum());
        assertThat(actual.getLimitCurrencyShortname()).isEqualTo(expected.getLimitCurrencyShortname());
        assertThat(actual.getExpenseCategory()).isEqualTo(expected.getExpenseCategory());
        assertThat(actual.getAccount().getId()).isEqualTo(expected.getAccount().getId());
    }

    protected static void assertTransaction(Transaction actual, Transaction expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExpenseCategory()).isEqualTo(expected.getExpenseCategory());
        assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        assertThat(actual.getCurrencyShortname()).isEqualTo(expected.getCurrencyShortname());
        assertThat(actual.isLimitExceeded()).isEqualTo(expected.isLimitExceeded());
        assertThat(actual.getDatetime()).isEqualTo(expected.getDatetime());
        assertThat(actual.getAccountFrom().getId()).isEqualTo(expected.getAccountFrom().getId());
        assertThat(actual.getAccountTo().getId()).isEqualTo(expected.getAccountTo().getId());
    }

}
