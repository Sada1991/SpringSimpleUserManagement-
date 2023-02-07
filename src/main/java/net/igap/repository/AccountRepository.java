package net.igap.repository;

import java.util.Optional;
import net.igap.model.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
  Optional<Account> findAccountByEnableIsTrueAndPersonNationalCode(Long nationalCode);
  Optional<Account> findAccountByEnableIsTrueAndId(Long nationalCode);
  Optional<Account> findAccountByEnableIsTrueAndUserName(String userName);
  Page<Account> findAllByEnableIsTrue(Pageable pageable);
}
