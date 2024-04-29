package org.example.testproject.repository.postgres;

import org.example.testproject.domain.model.postgres.custom.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
