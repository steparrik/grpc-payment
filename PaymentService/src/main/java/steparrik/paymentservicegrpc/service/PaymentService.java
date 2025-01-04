package steparrik.paymentservicegrpc.service;

import account.Account;
import account.AccountServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import steparrik.paymentservicegrpc.dto.PaymentDto;

@Service
public class PaymentService {

    @GrpcClient("accountServiceGrpc")
    private AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub;

    public PaymentDto makePayment(String userId, Integer amount) {
        Account.AccountRequest accountRequest = Account.AccountRequest.newBuilder().setUserId(userId).build();
        Account.AccountResponse accountResponse = accountServiceStub.getBalance(accountRequest);

        if (accountResponse.getBalance() >= amount) {
            Account.DeductRequest deductRequest = Account.DeductRequest.newBuilder().setUserId(userId).setAmount(amount).build();
            Account.DeductResponse deductResponse = accountServiceStub.deductBalance(deductRequest);

            if (deductResponse.getSuccess()) {
                return new PaymentDto(true, "Payment successful");
            } else {
                return new PaymentDto(false, deductResponse.getMessage());
            }
        } else {
            return new PaymentDto(false, "Insufficient balance for payment.");
        }
    }
}
