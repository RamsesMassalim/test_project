package org.example.testproject.repository.postgres;

import jakarta.transaction.Transactional;
import org.example.testproject.AbstractJpaTest;
import org.example.testproject.domain.enums.Currency;
import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.ExpenseLimit;
import org.example.testproject.domain.model.postgres.Transaction;
import org.example.testproject.domain.model.postgres.custom.IAmountCount;
import org.example.testproject.domain.model.postgres.custom.IExceededTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.testproject.domain.enums.Currency.*;

@Transactional
class TransactionRepositoryTest extends AbstractJpaTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void whenSave_thenReturnTransaction() {
        Transaction transaction = createTransaction("tr", "01");

        Transaction fromDb = transactionRepository.save(transaction);

        assertTransaction(fromDb, transaction);
    }

    @Test
    public void whenFindById_thenReturnTransaction() {
        Transaction transaction = createAndSaveTransaction("tr", "02");

        Transaction found = transactionRepository.findById(transaction.getId())
                .orElse(null);

        assertTransaction(found, transaction);
    }

    @Test
    public void whenCalculateTransaction_thenReturnAmountsByCurrency() {
        ZonedDateTime time = ZonedDateTime.now();

        Transaction transaction = createAndSaveTransaction("tr", "03", USD, BigDecimal.ONE, ExpenseCategory.GOODS, false);
        createAndSaveTransaction("tr", "03", USD, BigDecimal.TEN, ExpenseCategory.GOODS, false);

        createAndSaveTransaction("tr", "03", RUB, BigDecimal.ONE, ExpenseCategory.GOODS, false);
        createAndSaveTransaction("tr", "03", RUB, BigDecimal.ONE, ExpenseCategory.GOODS, false);

        createAndSaveTransaction("tr", "03", KZT, BigDecimal.TEN, ExpenseCategory.GOODS, false);
        createAndSaveTransaction("tr", "03", KZT, BigDecimal.TEN, ExpenseCategory.GOODS, false);

        List<IAmountCount> found = transactionRepository.calculateSumOfTransactions(transaction.getAccountFrom().getId(),
                ExpenseCategory.GOODS,
                time.plusMonths(1),
                time.minusHours(1));

        assertThat(found).hasSize(3);

        found.sort(Comparator.comparing(x -> x.getCurrency().name()));

        {
            IAmountCount count = found.get(0);

            assertThat(count).isNotNull();
            assertThat(count.getCurrency()).isEqualTo(KZT);
            assertThat(count.getAmount()).isEqualTo(BigDecimal.valueOf(20));
        }

        {
            IAmountCount count = found.get(1);

            assertThat(count).isNotNull();
            assertThat(count.getCurrency()).isEqualTo(RUB);
            assertThat(count.getAmount()).isEqualTo(BigDecimal.valueOf(2));
        }

        {
            IAmountCount count = found.get(2);

            assertThat(count).isNotNull();
            assertThat(count.getCurrency()).isEqualTo(USD);
            assertThat(count.getAmount()).isEqualTo(BigDecimal.valueOf(11));
        }
    }

    @Test
    public void whenFindExceededLimits_thenReturnExceededLimits() {
        ZonedDateTime time = ZonedDateTime.now();

        ExpenseLimit limit1 = createAndSaveLimit("testtrfr04", BigDecimal.valueOf(1000), Currency.USD, ExpenseCategory.GOODS, time, time.plusMonths(1));

        createAndSaveTransaction("tr", "04", USD, BigDecimal.valueOf(500), ExpenseCategory.GOODS, false);
        Transaction transaction2 = createAndSaveTransaction("tr", "04", USD, BigDecimal.valueOf(600), ExpenseCategory.GOODS, true);

        ExpenseLimit limit2 = createAndSaveLimit("testtrfr04", BigDecimal.valueOf(2000), Currency.USD, ExpenseCategory.GOODS, ZonedDateTime.now(), time.plusMonths(1));

        createAndSaveTransaction("tr", "04", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS, false);
        createAndSaveTransaction("tr", "04", USD, BigDecimal.valueOf(700), ExpenseCategory.GOODS, false);
        createAndSaveTransaction("tr", "04", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS, false);
        Transaction transaction6 = createAndSaveTransaction("tr", "04", USD, BigDecimal.valueOf(100), ExpenseCategory.GOODS, true);

        List<IExceededTransaction> found = transactionRepository.findAllExceededTransactions("testtrfr04");

        assertThat(found).hasSize(2);

        found.sort(Comparator.comparing(x -> x.getLimit().getId()));

        {
            IExceededTransaction exceededTransaction = found.get(0);

            assertThat(exceededTransaction).isNotNull();

            ExpenseLimit actualLimit = exceededTransaction.getLimit();
            Transaction transaction = exceededTransaction.getTransaction();

            assertLimit(actualLimit, limit1);
            assertTransaction(transaction, transaction2);
        }

        {
            IExceededTransaction exceededTransaction = found.get(1);

            assertThat(exceededTransaction).isNotNull();

            ExpenseLimit actualLimit = exceededTransaction.getLimit();
            Transaction transaction = exceededTransaction.getTransaction();

            assertLimit(actualLimit, limit2);
            assertTransaction(transaction, transaction6);
        }
    }


}
