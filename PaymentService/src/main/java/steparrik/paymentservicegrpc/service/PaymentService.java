package steparrik.paymentservicegrpc.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import steparrik.paymentservicegrpc.dto.AccountResponse;
import steparrik.paymentservicegrpc.dto.DeductRequest;
import steparrik.paymentservicegrpc.dto.DeductResponse;
import steparrik.paymentservicegrpc.dto.PaymentDto;

@Service
public class PaymentService {
    private RestTemplate restTemplate;

    private PaymentService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public PaymentDto makePayment(@PathVariable String userId, @RequestParam Integer amount) {
        DeductRequest deductRequest = new DeductRequest(userId, 1);

        ResponseEntity<DeductResponse> deductResponse = restTemplate.postForEntity(
                "http://localhost:8080/account/deduct",
                deductRequest,
                DeductResponse.class);;

        for(int i = 0;i<amount-1;i++){
            deductResponse =  restTemplate.postForEntity(
                    "http://localhost:8080/account/deduct",
                    deductRequest,
                    DeductResponse.class);
        }

        if (deductResponse.getBody() != null && deductResponse.getBody().isSuccess()) {
            return new PaymentDto(true, "Payment successful");
        } else {
            return new PaymentDto(false, deductResponse.getBody().getMessage());
        }
    }
}
