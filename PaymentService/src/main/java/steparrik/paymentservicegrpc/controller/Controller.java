package steparrik.paymentservicegrpc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import steparrik.paymentservicegrpc.dto.PaymentDto;
import steparrik.paymentservicegrpc.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final PaymentService paymentService;

    @PostMapping("/{userId}")
    private PaymentDto makePayment(@PathVariable String userId,
                                   @RequestParam Integer amount){
        return paymentService.makePayment(userId, amount);
    }
}
