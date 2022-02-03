package com.devexperts.model;

import com.devexperts.model.account.AccountKey;
import java.util.Objects;

public class BankAccountKey extends AccountKey {

    protected BankAccountKey(long accountId) {
        super(accountId);
    }

    public static BankAccountKey valueOf(long accountId) {
        return new BankAccountKey(accountId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankAccountKey that = (BankAccountKey) o;
        return accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
