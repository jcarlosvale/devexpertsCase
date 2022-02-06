package com.devexperts.service;

import com.devexperts.model.service.AccountService;

public interface BankAccountService extends AccountService {

    void transfer(long source, long target, double amount);

}
