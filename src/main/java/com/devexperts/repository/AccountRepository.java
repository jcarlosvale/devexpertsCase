package com.devexperts.repository;

import com.devexperts.model.BankAccount;
import com.devexperts.model.BankAccountKey;
import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

    private final Map<AccountKey, Account> accountsMap = new ConcurrentHashMap<>();

    public void clear() {
        accountsMap.clear();
    }

    public void save(final BankAccount bankAccount) {
        accountsMap.put(bankAccount.getAccountKey(), bankAccount);
    }

    public Account retrieve(long bankAccountKey) {
        return accountsMap.get(BankAccountKey.valueOf(bankAccountKey));
    }
}
