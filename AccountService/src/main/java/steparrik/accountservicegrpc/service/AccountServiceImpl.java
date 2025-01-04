
package steparrik.accountservicegrpc.service;

import account.Account.AccountRequest;
import account.Account.AccountResponse;
import account.Account.DeductRequest;
import account.Account.DeductResponse;
import account.AccountServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.HashMap;
import java.util.Map;

@GrpcService
public class AccountServiceImpl extends AccountServiceGrpc.AccountServiceImplBase {
    private final Map<String, Integer> userBalances = new HashMap<>();

    public AccountServiceImpl() {
        userBalances.put("1", 1000);
        userBalances.put("2", 500);
    }

    @Override
    public void getBalance(AccountRequest request, StreamObserver<AccountResponse> responseObserver) {
        String userId = request.getUserId();
        Integer balance = userBalances.getOrDefault(userId, 0);

        AccountResponse response = AccountResponse.newBuilder()
                .setUserId(userId)
                .setBalance(balance)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deductBalance(DeductRequest request, StreamObserver<DeductResponse> responseObserver) {
        String userId = request.getUserId();
        int amount = request.getAmount();

        Integer currentBalance = userBalances.getOrDefault(userId, 0);
        if (currentBalance >= amount) {
            userBalances.put(userId, currentBalance - amount);
            DeductResponse response = DeductResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Balance deducted successfully.")
                    .build();
            responseObserver.onNext(response);
        } else {
            DeductResponse response = DeductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Insufficient balance.")
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
        System.out.println(userBalances);
    }
}
