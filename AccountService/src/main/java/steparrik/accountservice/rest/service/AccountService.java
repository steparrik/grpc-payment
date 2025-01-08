package steparrik.accountservice.rest.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import steparrik.accountservice.rest.dto.AccountResponse;
import steparrik.accountservice.rest.dto.DeductRequest;
import steparrik.accountservice.rest.dto.DeductResponse;
import steparrik.accountservice.rest.model.Account;
import steparrik.accountservice.rest.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final EntityManager entityManager;

    public AccountResponse getBalance(@PathVariable String userId) {
        Account account = accountRepository.getAccountByUserId(userId);
        Integer balance = account != null ? account.getAmount() : 0;
        AccountResponse response = new AccountResponse(userId, balance);
        return response;
    }

    @Transactional
    public DeductResponse deductBalance(@RequestBody DeductRequest request) {
        Integer amount = request.getAmount();
        Account account =
                entityManager.find(
                        Account.class, request.getUserId(), LockModeType.PESSIMISTIC_WRITE);

        Integer currentBalance = account.getAmount();
        if (currentBalance >= amount) {
            currentBalance -= amount;
            account.setAmount(currentBalance);
            accountRepository.save(account);
            return new DeductResponse(true, "Balance deducted successfully.");
        } else {
            return new DeductResponse(false, "Insufficient balance.");
        }
    }
}
