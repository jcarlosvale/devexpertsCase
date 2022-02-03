package com.devexperts.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.devexperts.model.BankAccount;
import com.devexperts.model.account.Account;
import com.devexperts.model.service.AccountService;
import com.devexperts.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void clear() {
        accountRepository.clear();
    }

    @Override
    public void createAccount(final Account account) {

        checkNotNull(account, "account must not be null");
        checkArgument(account instanceof BankAccount, "account should be an instance of BankAccount");

        BankAccount bankAccount = (BankAccount) account;
        accountRepository.save(bankAccount);
    }

    @Override
    public Account getAccount(long id) {
        return accountRepository.retrieve(id);
    }

    @Override
    public void transfer(final Account source, final Account target, double amount) {
        //do nothing for now
    }
}
