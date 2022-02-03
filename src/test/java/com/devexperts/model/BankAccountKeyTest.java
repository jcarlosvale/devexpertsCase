package com.devexperts.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BankAccountKeyTest {

    @Test
    void isEqual() {
        //GIVEN
        BankAccountKey bankAccountKeyOne =  BankAccountKey.valueOf(234L);
        BankAccountKey bankAccountKeyTwo = new BankAccountKey(234L);

        //WHEN THEN
        assertThat(bankAccountKeyOne.equals(bankAccountKeyTwo)).isTrue();
    }

    @Test
    void isNotEqual() {
        //GIVEN
        BankAccountKey bankAccountKeyOne =  BankAccountKey.valueOf(235L);
        BankAccountKey bankAccountKeyTwo = new BankAccountKey(234L);

        //WHEN THEN
        assertThat(bankAccountKeyOne.equals(bankAccountKeyTwo)).isFalse();
    }
}