package steparrik.accountservice.rest.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import steparrik.accountservice.rest.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Account getAccountByUserId(String userId);
}
