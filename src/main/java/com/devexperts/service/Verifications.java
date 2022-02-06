package com.devexperts.service;

import com.devexperts.exceptions.AccountIsNotBankAccountException;
import com.devexperts.exceptions.AccountIsNullException;
import com.devexperts.exceptions.InvalidAmountException;
import com.devexperts.model.BankAccount;
import com.devexperts.model.account.Account;

public class Verifications {

    public static void verifyAccountIsNotNull(final Account account) {
        if (account == null) throw new AccountIsNullException();
    }

    public static void verifyAccountIsInstanceOfBankAccount(final Account account) {
        if (!(account instanceof BankAccount)) throw new AccountIsNotBankAccountException();
    }

    public static void verifyIsValidAmount(final double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
    }
}
