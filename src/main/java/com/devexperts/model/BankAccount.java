package com.devexperts.model;

import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankAccount that = (BankAccount) o;
        return Objects.equals(bankAccountKey, that.bankAccountKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankAccountKey);
    }
}
