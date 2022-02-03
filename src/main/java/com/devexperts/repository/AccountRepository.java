package com.devexperts.repository;

import static com.google.common.base.Preconditions.checkArgument;

import com.devexperts.model.BankAccount;
import com.devexperts.model.BankAccountKey;
import com.devexperts.model.account.AccountKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

    private final Map<AccountKey, BankAccount> accountsMap = new ConcurrentHashMap<>();

    public void clear() {
        accountsMap.clear();
    }

    public void save(final BankAccount bankAccount) {
        accountsMap.put(bankAccount.getAccountKey(), bankAccount);
    }

    public BankAccount retrieve(final AccountKey accountKey) {
        checkArgument(accountKey instanceof BankAccountKey, "accountKey should be an instance of BankAccountKey");
        BankAccountKey bankAccountKey = (BankAccountKey) accountKey;
        return accountsMap.get(bankAccountKey);
    }
}
