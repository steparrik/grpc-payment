package steparrik.paymentservicegrpc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import steparrik.paymentservicegrpc.dto.PaymentDto;
import steparrik.paymentservicegrpc.service.PaymentService;

@RestController
@RequestMapping("/payment/rest/")
@RequiredArgsConstructor
public class RestAPIController {
    private final PaymentService paymentService;

    @GetMapping("/{userId}")
    public PaymentDto makePayment(@PathVariable String userId, @RequestParam Integer amount) {
        return paymentService.makePayment(userId, amount);
    }
}
