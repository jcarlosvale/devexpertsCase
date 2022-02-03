package com.devexperts.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BankAccountTest {

    @Test
    void constructorSuccessful() {
        //GIVEN
        final long accountId = 1L;
        final var accountKey = BankAccountKey.valueOf(accountId);
        final String firstName = "some first name";
        final String lastName = "some last name";
        final Double balance = 1D;

        //WHEN
        final var bankAccount = new BankAccount(accountId, firstName, lastName, balance);

        //THEN
        assertThat(bankAccount.getAccountKey())
                .isEqualTo(accountKey);
        assertThat(bankAccount.getFirstName())
                .isEqualTo(firstName);
        assertThat(bankAccount.getLastName())
                .isEqualTo(lastName);
        assertThat(bankAccount.getBalance())
                .isEqualTo(balance);
    }
}