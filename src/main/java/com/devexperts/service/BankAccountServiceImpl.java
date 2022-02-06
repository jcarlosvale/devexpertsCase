package com.devexperts.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.devexperts.exceptions.InsufficientAccountBalanceException;
import com.devexperts.model.BankAccount;
import com.devexperts.model.BankAccountKey;
import com.devexperts.model.account.Account;
import com.devexperts.repository.AccountRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final AccountRepository accountRepository;

    public BankAccountServiceImpl(final AccountRepository accountRepository) {
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

        Verifications.verifyAccountIsNotNull(source);
        Verifications.verifyAccountIsNotNull(target);

        Verifications.verifyAccountIsInstanceOfBankAccount(source);
        Verifications.verifyAccountIsInstanceOfBankAccount(target);

        BankAccountKey sourceBankAccountKey = (BankAccountKey) source.getAccountKey();
        BankAccountKey targetBankAccountKey = (BankAccountKey) target.getAccountKey();

        transfer(sourceBankAccountKey.getAccountId(), targetBankAccountKey.getAccountId(), amount);
    }

    @Override
    public void transfer(final long source, final long target, final double amount) {

        Verifications.verifyIsValidAmount(amount);

        final Account accountSource = accountRepository.retrieve(BankAccountKey.valueOf(source));
        final Account accountTarget = accountRepository.retrieve(BankAccountKey.valueOf(target));

        Verifications.verifyAccountIsNotNull(accountSource);
        Verifications.verifyAccountIsNotNull(accountTarget);

        if (amount > accountSource.getBalance()) {
            throw new InsufficientAccountBalanceException(
                    String.format("Insufficient Account Balance from source account %s, amount: %f",
                            source, amount));
        } else {
            synchronized (accountSource) {
                synchronized (accountTarget) {

                    accountSource.setBalance(
                            BigDecimal
                            .valueOf(accountSource.getBalance() - amount)
                            .setScale(2, RoundingMode.HALF_EVEN)
                            .doubleValue());

                    accountTarget.setBalance(BigDecimal
                            .valueOf(accountTarget.getBalance() + amount)
                            .setScale(2, RoundingMode.HALF_EVEN)
                            .doubleValue());
                }
            }
        }
    }
}
