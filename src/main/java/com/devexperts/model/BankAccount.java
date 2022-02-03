package com.devexperts.model;

import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;

public class BankAccount extends Account {

    private final BankAccountKey bankAccountKey;

    public BankAccount(long accountId, final String firstName, final String lastName, final Double balance) {

        super(AccountKey.valueOf(accountId), firstName, lastName, balance);
        this.bankAccountKey = new BankAccountKey(accountId);
    }

    @Override
    public AccountKey getAccountKey() {
        return bankAccountKey;
    }
}
