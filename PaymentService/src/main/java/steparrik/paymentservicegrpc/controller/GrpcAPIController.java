package steparrik.paymentservicegrpc.controller;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import steparrik.paymentservicegrpc.dto.PaymentDto;
import steparrik.paymentservicegrpc.service.PaymentGrpcService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/grpc/")
public class GrpcAPIController {
    private final PaymentGrpcService paymentService;

    @GetMapping("/{userId}")
    private PaymentDto makePayment(@PathVariable String userId, @RequestParam Integer amount) {
        return paymentService.makePayment(userId, amount);
    }

    @GetMapping("bi/{userId}")
    private CompletableFuture<PaymentDto> makePaymentBi(
            @PathVariable String userId, @RequestParam Integer amount) {
        return paymentService.makePaymentBi(userId, amount);
    }
}
