package steparrik.accountservice.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import steparrik.accountservice.rest.dto.AccountResponse;
import steparrik.accountservice.rest.dto.DeductRequest;
import steparrik.accountservice.rest.dto.DeductResponse;
import steparrik.accountservice.rest.service.AccountService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class Controller {
    private final AccountService accountServiceRest;

    @GetMapping("/balance/{userId}")
    public AccountResponse getBalance(@PathVariable String userId) {
        return accountServiceRest.getBalance(userId);
    }

    @PostMapping("/deduct")
    public DeductResponse deductBalance(@RequestBody DeductRequest request) {
        return accountServiceRest.deductBalance(request);
    }
}
