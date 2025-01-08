package steparrik.accountservice.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import steparrik.accountservice.rest.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    Account getAccountByUserId(String userId);

    @Query("SELECT a.amount FROM Account a WHERE a.userId = :userId")
    Integer findAmountByUserId(@Param("userId") String userId);
}
