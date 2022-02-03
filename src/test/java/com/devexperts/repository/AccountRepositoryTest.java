package com.devexperts.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devexperts.model.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository repository;

    @Test
    void savingAndRetrievingAnAccount() {
        //GIVEN
        long id = 1L;
        BankAccount bankAccount = new BankAccount(id, "some firstname", "some lastName", 1.01);
        repository.save(bankAccount);
        //WHEN
        final var actualBankAccount = repository.retrieve(id);
        //THEN
        assertThat(actualBankAccount).isEqualTo(bankAccount);
    }

    @Test
    void clearRepository() {
        //GIVEN
        long id = 1L;
        BankAccount bankAccount = new BankAccount(id, "some firstname", "some lastName", 1.01);
        repository.save(bankAccount);
        //WHEN
        repository.clear();
        final var actualBankAccount = repository.retrieve(id);
        //THEN
        assertThat(actualBankAccount).isNull();
    }
}