package org.example.testproject.service;

import org.example.testproject.AbstractServiceTest;
import org.example.testproject.domain.dto.ExpenseLimitDTO;
import org.example.testproject.domain.dto.TransactionDTO;
import org.example.testproject.domain.enums.Currency;
import org.example.testproject.domain.enums.ExpenseCategory;
import org.example.testproject.domain.model.postgres.Transaction;
import org.example.testproject.domain.model.postgres.custom.IAmountCount;
import org.example.testproject.mapper.AccountMapper;
import org.example.testproject.mapper.ExpenseLimitMapper;
import org.example.testproject.mapper.TransactionMapper;
import org.example.testproject.repository.postgres.TransactionRepository;
import org.example.testproject.service.imp.TransactionServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.testproject.domain.enums.Currency.USD;
import static org.mockito.ArgumentMatchers.any;

class TransactionServiceTest extends AbstractServiceTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public TransactionService transactionService() {
            return new TransactionServiceImp();
        }
    }

    @Autowired
    private TransactionService transactionService;

    @MockBean
    TransactionRepository transactionRepository;

    @MockBean
    private ExpenseLimitService expenseLimitService;

    @MockBean
    private CurrencyRateService currencyRateService;

    @MockBean
    private TransactionMapper transactionMapper;

    @MockBean
    private ExpenseLimitMapper limitMapper;

    @MockBean
    private AccountMapper accountMapper;

    @Test
    public void whenSave_thenChangeLimitAndDate() {
        ZonedDateTime time = ZonedDateTime.now();

        TransactionDTO transaction = createTransactionDTO("ts", "01", USD, BigDecimal.valueOf(500), ExpenseCategory.GOODS);
        transaction.setDatetime(null);
        transaction.setId(1L);

        ExpenseLimitDTO limit = createLimitDto("testtsfr01", BigDecimal.valueOf(1000), Currency.USD, ExpenseCategory.GOODS, time, time.plusMonths(1));

        AtomicReference<Boolean> isExceeded = new AtomicReference<>();
        AtomicReference<ZonedDateTime> dateTime = new AtomicReference<>();

        //
        //

        Mockito.when(expenseLimitService.getLimitForCategoryOrCreateNew(transaction.getAccountFrom(), transaction.getExpenseCategory()))
                .thenReturn(limit);

        Mockito.when(transactionRepository.calculateSumOfTransactions(any(), any(), any(), any()))
                .thenReturn(List.of(new IAmountCount() {
                    @Override
                    public Currency getCurrency() {
                        return USD;
                    }

                    @Override
                    public BigDecimal getAmount() {
                        return BigDecimal.valueOf(500);
                    }
                }));

        Mockito.when(transactionRepository.save(any())).then((Answer<Transaction>) invocation -> {
            Transaction argument = invocation.getArgument(0);

            isExceeded.set(argument.isLimitExceeded());
            dateTime.set(argument.getDatetime());

            return getTransactionWithId(argument, 1L);
        });

        Mockito.when(transactionMapper.toDto(any())).thenReturn(transaction);
        Mockito.when(transactionMapper.toEntity(any())).thenReturn(new Transaction());

        //
        //
        TransactionDTO transactionDTO = transactionService.saveTransaction(transaction);
        //
        //

        assertTransaction(transactionDTO, transaction);
        assertThat(isExceeded.get()).isFalse();
        assertThat(dateTime.get()).isNotNull();
    }

    private Transaction getTransactionWithId(Transaction transaction, Long id) {
        transaction.setId(id);

        return transaction;
    }
}