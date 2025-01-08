package steparrik.accountservice.grpc.service;

import account.Account.AccountRequest;
import account.Account.AccountResponse;
import account.Account.DeductRequest;
import account.Account.DeductResponse;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import steparrik.accountservice.rest.model.Account;
import steparrik.accountservice.rest.repository.AccountRepository;

@GrpcService
@RequiredArgsConstructor
public class AccountGrpcService extends account.AccountServiceGrpc.AccountServiceImplBase {
    private final AccountRepository accountRepository;
    private final EntityManager entityManager;
    private final PlatformTransactionManager transactionManager;

    @Override
    @Transactional
    public void getBalance(
            AccountRequest request, StreamObserver<AccountResponse> responseObserver) {
        String userId = request.getUserId();
        Integer balance = accountRepository.getAccountByUserId(userId).getAmount();

        AccountResponse response =
                AccountResponse.newBuilder().setUserId(userId).setBalance(balance).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void deductBalance(
            DeductRequest request, StreamObserver<DeductResponse> responseObserver) {
        int amount = request.getAmount();

        Account account =
                entityManager.find(
                        Account.class, request.getUserId(), LockModeType.PESSIMISTIC_WRITE);
        Integer currentBalance = account.getAmount();
        if (currentBalance >= amount) {
            currentBalance -= amount;
            account.setAmount(currentBalance);
            accountRepository.save(account);

            DeductResponse response =
                    DeductResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("Balance deducted successfully.")
                            .build();
            responseObserver.onNext(response);
        } else {
            DeductResponse response =
                    DeductResponse.newBuilder()
                            .setSuccess(false)
                            .setMessage("Insufficient balance.")
                            .build();
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public StreamObserver<AccountRequest> getBalanceBi(
            StreamObserver<AccountResponse> responseObserver) {
        return new StreamObserver<AccountRequest>() {
            @Override
            public void onNext(AccountRequest request) {
                String userId = request.getUserId();
                Integer balance = accountRepository.getAccountByUserId(userId).getAmount();

                AccountResponse response =
                        AccountResponse.newBuilder().setUserId(userId).setBalance(balance).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<DeductRequest> deductBalanceBi(
            StreamObserver<DeductResponse> responseObserver) {
        return new StreamObserver<DeductRequest>() {
            @Override
            public void onNext(DeductRequest request) {
                TransactionStatus status =
                        transactionManager.getTransaction(new DefaultTransactionDefinition());
                try {
                    String userId = request.getUserId();
                    int amount = request.getAmount();

                    Account account =
                            entityManager.find(
                                    Account.class, userId, LockModeType.PESSIMISTIC_WRITE);
                    Integer currentBalance = account.getAmount();

                    DeductResponse response;
                    if (currentBalance >= amount) {
                        currentBalance -= amount;
                        account.setAmount(currentBalance);
                        accountRepository.save(account);

                        response =
                                DeductResponse.newBuilder()
                                        .setSuccess(true)
                                        .setMessage("Balance deducted successfully.")
                                        .build();
                    } else {
                        response =
                                DeductResponse.newBuilder()
                                        .setSuccess(false)
                                        .setMessage("Insufficient balance.")
                                        .build();
                    }
                    responseObserver.onNext(response);
                    transactionManager.commit(status);
                } catch (Exception e) {
                    transactionManager.rollback(status);
                    responseObserver.onError(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
