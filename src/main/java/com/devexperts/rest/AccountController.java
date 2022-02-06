package com.devexperts.rest;

import com.devexperts.model.rest.AbstractAccountController;
import com.devexperts.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations/")
public class AccountController implements AbstractAccountController {

    private final BankAccountService bankAccountService;

    public AccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    @PostMapping(
            path = "/transfer"
    )
    public ResponseEntity<Void> transfer(@RequestParam final long sourceId,
            @RequestParam final long targetId, @RequestParam final double amount) {
        bankAccountService.transfer(sourceId, targetId, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
