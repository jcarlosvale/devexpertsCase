package com.devexperts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.devexperts.model.BankAccount;
import com.devexperts.model.account.Account;
import com.devexperts.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private Account account;
    @Mock
    private BankAccount bankAccount;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void clear() {
        //GIVEN
        //WHEN
        accountService.clear();
        //THEN
        verify(accountRepository).clear();
    }

    @Test
    void createAccountRejectsNotBankAccount() {
        //GIVEN
        //WHEN
        //THEN
        assertThatThrownBy(() -> accountService.createAccount(account))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createAccountRejectsNull() {
        //GIVEN
        //WHEN
        //THEN
        assertThatThrownBy(() -> accountService.createAccount(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void createAccount() {
        //GIVEN
        //WHEN
        accountService.createAccount(bankAccount);
        //THEN
        verify(accountRepository).save(bankAccount);
    }

    @Test
    void getAccount() {
        //GIVEN
        long id = 123L;
        given(accountRepository.retrieve(id)).willReturn(bankAccount);

        //WHEN
        Account actualAccount = accountService.getAccount(id);

        //THEN
        assertThat(actualAccount).isEqualTo(bankAccount);
    }
}