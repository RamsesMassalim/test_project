package org.example.testproject.repository.postgres;

import jakarta.transaction.Transactional;
import org.example.testproject.AbstractJpaTest;
import org.example.testproject.domain.enums.Currency;
import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.ExpenseLimit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CurrencyLimitRepositoryTest extends AbstractJpaTest {

    @Autowired
    private ExpenseLimitRepository limitRepository;

    @Test
    public void whenSave_thenReturnExpenseLimit() {
        ExpenseLimit limit = createLimit("cl", "01");

        ExpenseLimit fromDb = limitRepository.save(limit);

        assertLimit(fromDb, limit);
    }

    @Test
    public void whenFindById_thenReturnExpenseLimit() {
        ExpenseLimit limit = createAndSaveLimit("cl", "02");

        ExpenseLimit found = limitRepository.findById(limit.getId())
                .orElse(null);

        assertLimit(found, limit);
    }

    @Test
    public void whenFindAllByAcc_thenReturnAllExpenseLimit() {
        ExpenseLimit expectedLimit1 = createAndSaveLimit("cl", "03");
        ExpenseLimit expectedLimit2 = createAndSaveLimit("cl", "03", BigDecimal.ONE, Currency.KZT, ExpenseCategory.GOODS);

        List<ExpenseLimit> found = limitRepository.findAllByAccountId(expectedLimit1.getAccount().getId());

        assertThat(found).hasSize(2);

        {
            ExpenseLimit limit = found.get(0);

            assertLimit(limit, expectedLimit1);
        }

        {
            ExpenseLimit limit = found.get(1);

            assertLimit(limit, expectedLimit2);
        }
    }

    @Test
    public void whenFindByAcc_thenReturnLatestExpenseLimit() {
        ExpenseLimit expectedLimit1 = createAndSaveLimit("cl", "04");
        ExpenseLimit expectedLimit2 = createAndSaveLimit("cl", "04");

        List<ExpenseLimit> found = limitRepository.findExpenseLimitsByAccountId(expectedLimit1.getAccount().getId());

        assertThat(found).hasSize(1);

        {
            ExpenseLimit limit = found.get(0);

            assertLimit(limit, expectedLimit2);
        }
    }

    @Test
    public void whenFindByAcc_thenReturnAllLatestForEachCategory() {
        createAndSaveLimit("cl", "05");
        ExpenseLimit expectedLimit1 = createAndSaveLimit("cl", "05");

        createAndSaveLimit("cl", "05", BigDecimal.ONE, Currency.KZT, ExpenseCategory.GOODS);
        ExpenseLimit expectedLimit2 = createAndSaveLimit("cl", "05", BigDecimal.ONE, Currency.KZT, ExpenseCategory.GOODS);

        List<ExpenseLimit> found = limitRepository.findExpenseLimitsByAccountId(expectedLimit1.getAccount().getId());

        assertThat(found).hasSize(2);

        {
            ExpenseLimit limit = found.get(0);

            assertLimit(limit, expectedLimit1);
        }

        {
            ExpenseLimit limit = found.get(1);

            assertLimit(limit, expectedLimit2);
        }
    }

    @Test
    public void whenFindActualByAcc_thenReturnActualForEachCategory() {
        createAndSaveLimit("cl", "06");
        ExpenseLimit expectedLimit1 = createAndSaveLimit("cl", "06");

        createAndSaveLimit("cl", "06", BigDecimal.ONE, Currency.KZT, ExpenseCategory.GOODS);
        ExpenseLimit expectedLimit2 = createAndSaveLimit("cl", "06", BigDecimal.ONE, Currency.KZT, ExpenseCategory.GOODS);

        {
            ExpenseLimit found = limitRepository.findActualLimitForCategory(expectedLimit1.getAccount().getId(), expectedLimit1.getExpenseCategory())
                    .orElse(null);

            assertLimit(found, expectedLimit1);
        }

        {
            ExpenseLimit found = limitRepository.findActualLimitForCategory(expectedLimit1.getAccount().getId(), expectedLimit2.getExpenseCategory())
                    .orElse(null);

            assertLimit(found, expectedLimit2);
        }
    }

}
