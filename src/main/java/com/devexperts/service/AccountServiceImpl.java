package com.devexperts.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.devexperts.exceptions.InsufficientAccountBalanceException;
import com.devexperts.model.BankAccount;
import com.devexperts.model.BankAccountKey;
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
    public Account getAccount(final long id) {
        return accountRepository.retrieve(BankAccountKey.valueOf(id));
    }

    @Override
    public void transfer(final Account source, final Account target, final double amount) {

        Verifications.verifyIsValidAmount(amount);

        final Account accountSource = getBankAccountAccount(source);
        final Account accountTarget = getBankAccountAccount(target);

        if (amount > accountSource.getBalance()) {
            throw new InsufficientAccountBalanceException(
                    String.format("Insufficient Account Balance from source account %s, amount: %f",
                            source, amount));
        } else {
            accountSource.setBalance(accountSource.getBalance() - amount);
            accountTarget.setBalance(accountTarget.getBalance() + amount);
        }

    }

    public BankAccount getBankAccountAccount(final Account account) {

        Verifications.verifyAccountIsNotNull(account);
        Verifications.verifyAccountIsInstanceOfBankAccount(account);

        return accountRepository.retrieve(account.getAccountKey());
    }


}
