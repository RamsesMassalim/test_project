package org.example.testproject.service;

import jakarta.transaction.Transactional;
import org.example.testproject.AbstractServiceTest;
import org.example.testproject.domain.dto.ExceededTransactionDTO;
import org.example.testproject.domain.dto.ExpenseLimitDTO;
import org.example.testproject.domain.dto.TransactionDTO;
import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.custom.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.testproject.domain.enums.Currency.USD;

@Transactional
public class TransactionServiceExceededTest extends AbstractServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    public void whenLimitExceed_thenReturn_case1() {
        ZonedDateTime time = ZonedDateTime.now();

        saveAccount(new Account("testtsto01"));

        ExpenseLimitDTO limit1 = createAndSaveLimitDto("testtsfr01", BigDecimal.valueOf(1000), USD, ExpenseCategory.GOODS, time, time.plusMonths(1));

        TransactionDTO transaction1 = createTransactionDTO("ts", "01", USD, BigDecimal.valueOf(500), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction1);
        //
        //

        TransactionDTO transaction2 = createTransactionDTO("ts", "01", USD, BigDecimal.valueOf(600), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction2);
        //
        //

        ExpenseLimitDTO limit2 = createAndSaveLimitDto("testtsfr01", BigDecimal.valueOf(2000), USD, ExpenseCategory.GOODS, ZonedDateTime.now(), time.plusMonths(1));

        TransactionDTO transaction3 = createTransactionDTO("ts", "01", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction3);
        //
        //

        TransactionDTO transaction4 = createTransactionDTO("ts", "01", USD, BigDecimal.valueOf(700), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction4);
        //
        //

        TransactionDTO transaction5 = createTransactionDTO("ts", "01", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction5);
        //
        //

        TransactionDTO transaction6 = createTransactionDTO("ts", "01", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction6);
        //
        //

        //
        //
        List<ExceededTransactionDTO> list = transactionService.findExceededTransactions("testtsfr01");
        //
        //

        assertThat(list).hasSize(2);

        list.sort(Comparator.comparing(x -> x.getLimit().getId()));

        {
            ExceededTransactionDTO exceededTransaction = list.get(0);

            assertExceededTransaction(exceededTransaction, limit1, transaction2);
        }

        {
            ExceededTransactionDTO exceededTransaction = list.get(1);

            assertExceededTransaction(exceededTransaction, limit2, transaction6);
        }

    }

    @Test
    public void whenLimitExceed_thenReturn_case2() {
        ZonedDateTime time = ZonedDateTime.now();

        saveAccount(new Account("testtsto02"));

        createAndSaveLimitDto("testtsfr02", BigDecimal.valueOf(1000), USD, ExpenseCategory.GOODS, time, time.plusMonths(1));

        TransactionDTO transaction1 = createTransactionDTO("ts", "02", USD, BigDecimal.valueOf(500), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction1);
        //
        //

        TransactionDTO transaction2 = createTransactionDTO("ts", "02", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction2);
        //
        //


        ExpenseLimitDTO limit2 = createAndSaveLimitDto("testtsfr02", BigDecimal.valueOf(400), USD, ExpenseCategory.GOODS, ZonedDateTime.now(), time.plusMonths(1));

        TransactionDTO transaction3 = createTransactionDTO("ts", "02", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction3);
        //
        //


        TransactionDTO transaction4 = createTransactionDTO("ts", "02", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS);

        //
        //
        transactionService.saveTransaction(transaction4);
        //
        //

        //
        //
        List<ExceededTransactionDTO> list = transactionService.findExceededTransactions("testtsfr02");
        //
        //

        assertThat(list).hasSize(2);

        list.sort(Comparator.comparing(x -> x.getLimit().getId()));

        {
            ExceededTransactionDTO exceededTransaction = list.get(0);

            assertExceededTransaction(exceededTransaction, limit2, transaction3);
        }

        {
            ExceededTransactionDTO exceededTransaction = list.get(1);

            assertExceededTransaction(exceededTransaction, limit2, transaction4);
        }
    }

    @Test
    public void whenLimitExceeded_thenReturn() {
        ZonedDateTime time = ZonedDateTime.now();

        ExpenseLimitDTO limit1 = createAndSaveLimitDto("testtsfr03", BigDecimal.valueOf(1000), USD, ExpenseCategory.GOODS, time, time.plusMonths(1));

        createAndSaveTransactionDTO("ts", "03", USD, BigDecimal.valueOf(500), ExpenseCategory.GOODS);
        TransactionDTO transaction2 = createAndSaveTransactionDTO("ts", "03", USD, BigDecimal.valueOf(600), ExpenseCategory.GOODS, true);

        ExpenseLimitDTO limit2 = createAndSaveLimitDto("testtsfr03", BigDecimal.valueOf(2000), USD, ExpenseCategory.GOODS, ZonedDateTime.now(), time.plusMonths(1));

        createAndSaveTransactionDTO("ts", "03", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS);
        createAndSaveTransactionDTO("ts", "03", USD, BigDecimal.valueOf(700), ExpenseCategory.GOODS);
        createAndSaveTransactionDTO("ts", "03", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS);
        TransactionDTO transaction6 = createAndSaveTransactionDTO("ts", "03", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS, true);

        //
        //
        List<ExceededTransactionDTO> list = transactionService.findExceededTransactions("testtsfr03");
        //
        //

        assertThat(list).hasSize(2);

        list.sort(Comparator.comparing(x -> x.getLimit().getId()));

        {
            ExceededTransactionDTO exceededTransaction = list.get(0);

            assertExceededTransaction(exceededTransaction, limit1, transaction2);
        }

        {
            ExceededTransactionDTO exceededTransaction = list.get(1);

            assertExceededTransaction(exceededTransaction, limit2, transaction6);
        }
    }

    private void assertExceededTransaction(ExceededTransactionDTO exceededTransaction, ExpenseLimitDTO expectedLimit, TransactionDTO expectedTransaction) {
        assertThat(exceededTransaction).isNotNull();

        ExpenseLimitDTO actualLimit = exceededTransaction.getLimit();
        TransactionDTO transaction = exceededTransaction.getTransaction();

        assertLimit(actualLimit, expectedLimit);
        assertTransaction(transaction, expectedTransaction);
    }
}
