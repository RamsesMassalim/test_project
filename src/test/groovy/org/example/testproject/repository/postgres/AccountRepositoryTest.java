package org.example.testproject.repository.postgres;

import jakarta.transaction.Transactional;
import org.example.testproject.AbstractJpaTest;
import org.example.testproject.domain.model.postgres.custom.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class AccountRepositoryTest extends AbstractJpaTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void whenSave_thenReturnAccount() {
        Account account = new Account("test000001");

        Account fromDb = accountRepository.save(account);

        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getId()).isEqualTo(account.getId());
    }

    @Test
    public void whenFindById_thenReturnAccount() {
        Account account = saveAccount("ac", "00", "02");

        Account found = accountRepository.findById(account.getId())
                .orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(account.getId());
    }

}
