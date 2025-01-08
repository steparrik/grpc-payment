package steparrik.paymentservicegrpc.service;

import account.Account;
import account.AccountServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import steparrik.paymentservicegrpc.dto.PaymentDto;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentGrpcService {

    @GrpcClient("accountServiceGrpc")
    private AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub;

    @GrpcClient("accountServiceGrpc")
    private AccountServiceGrpc.AccountServiceStub asyncAccountServiceStub;

    public PaymentDto makePayment(String userId, Integer amount) {
        Account.DeductRequest deductRequest =
                Account.DeductRequest.newBuilder().setUserId(userId).setAmount(1).build();
        Account.DeductResponse deductResponse = accountServiceStub.deductBalance(deductRequest);

        for(int i = 0;i<amount-1;i++){
            deductResponse = accountServiceStub.deductBalance(deductRequest);
        }

        if (deductResponse.getSuccess()) {
            return new PaymentDto(true, "Payment successful");
        } else {
            return new PaymentDto(false, deductResponse.getMessage());
        }
    }

    public CompletableFuture<PaymentDto> makePaymentBi(String userId, Integer amount) {
        CompletableFuture<PaymentDto> completableFuture = new CompletableFuture<>();

        StreamObserver<Account.DeductRequest> deductRequestStream =
                asyncAccountServiceStub.deductBalanceBi(
                        new StreamObserver<Account.DeductResponse>() {
                            @Override
                            public void onNext(
                                    Account.DeductResponse deductResponse) {
                                if (deductResponse.getSuccess()) {
                                    completableFuture.complete(
                                            new PaymentDto(
                                                    true,
                                                    "Payment successful"));
                                } else {
                                    completableFuture.complete(
                                            new PaymentDto(
                                                    false,
                                                    deductResponse
                                                            .getMessage()));
                                }
                            }
                            @Override
                            public void onError(Throwable t) {
                                completableFuture.completeExceptionally(t);
                            }
                            @Override
                            public void onCompleted() {}
                        });

        for(int i = 0;i<amount;i++) {
            deductRequestStream.onNext(
                    Account.DeductRequest.newBuilder()
                            .setUserId(userId)
                            .setAmount(1)
                            .build());
        }
        deductRequestStream.onCompleted();


        return completableFuture;
    }
}
