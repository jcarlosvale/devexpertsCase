package com.devexperts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.devexperts.exceptions.AccountIsNotBankAccountException;
import com.devexperts.exceptions.AccountIsNullException;
import com.devexperts.exceptions.InsufficientAccountBalanceException;
import com.devexperts.exceptions.InvalidAmountException;
import com.devexperts.model.BankAccount;
import com.devexperts.model.BankAccountKey;
import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import com.devexperts.repository.AccountRepository;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
        given(accountRepository.retrieve(BankAccountKey.valueOf(id))).willReturn(bankAccount);

        //WHEN
        Account actualAccount = accountService.getAccount(id);

        //THEN
        assertThat(actualAccount).isEqualTo(bankAccount);
    }

    @ParameterizedTest
    @MethodSource("generateTransfers")
    void validTransfers(final BankAccount source, final BankAccount target, final double amount,
            final Account expectedSource, final Account expectedTarget) {
        //GIVEN
        given(accountRepository.retrieve(source.getAccountKey()))
                .willReturn(source);
        given(accountRepository.retrieve(target.getAccountKey()))
                .willReturn(target);

        //WHEN
        accountService.transfer(source, target, amount);

        //THEN
        assertThat(source).isEqualTo(expectedSource);
        assertThat(target).isEqualTo(expectedTarget);
    }

    private static Stream<Arguments> generateTransfers() {
        return Stream.of(
                Arguments.of(
                        new BankAccount(1L, "source firstname", "source lastname", 1D),
                        new BankAccount(2L, "target firstname", "target lastname", 0D),
                        1D,
                        new BankAccount(1L, "source firstname", "source lastname", 0D),
                        new BankAccount(2L, "target firstname", "target lastname", 1D)
                ),
                Arguments.of(
                        new BankAccount(1L, "source firstname", "source lastname", 1.01D),
                        new BankAccount(2L, "target firstname", "target lastname", 0.99D),
                        0.1D,
                        new BankAccount(1L, "source firstname", "source lastname", 1D),
                        new BankAccount(2L, "target firstname", "target lastname", 1D)
                ),
                Arguments.of(
                        new BankAccount(1L, "source firstname", "source lastname", 1.10D),
                        new BankAccount(2L, "target firstname", "target lastname", 1.10D),
                        0.01D,
                        new BankAccount(1L, "source firstname", "source lastname", 1.99D),
                        new BankAccount(2L, "target firstname", "target lastname", 1.11D)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("generateInsufficientBalanceTransfers")
    void transferInsufficientBalanceNotAllowed(final BankAccount source, final BankAccount target, final double amount,
            final Account expectedSource, final Account expectedTarget) {
        //GIVEN
        given(accountRepository.retrieve(source.getAccountKey()))
                .willReturn(source);
        given(accountRepository.retrieve(target.getAccountKey()))
                .willReturn(target);

        //WHEN THEN
        assertThatThrownBy(() -> accountService.transfer(source, target, amount))
                .isInstanceOf(InsufficientAccountBalanceException.class);
    }

    private static Stream<Arguments> generateInsufficientBalanceTransfers() {
        return Stream.of(
                Arguments.of(
                        new BankAccount(1L, "source firstname", "source lastname", 1D),
                        new BankAccount(2L, "target firstname", "target lastname", 0D),
                        1.01D,
                        new BankAccount(1L, "source firstname", "source lastname", 1D),
                        new BankAccount(2L, "target firstname", "target lastname", 0D)
                ),
                Arguments.of(
                        new BankAccount(1L, "source firstname", "source lastname", 1.01D),
                        new BankAccount(2L, "target firstname", "target lastname", 0.99D),
                        1.02D,
                        new BankAccount(1L, "source firstname", "source lastname", 1.01D),
                        new BankAccount(2L, "target firstname", "target lastname", 0.99D)
                ),
                Arguments.of(
                        new BankAccount(1L, "source firstname", "source lastname", 1.10D),
                        new BankAccount(2L, "target firstname", "target lastname", 1.10D),
                        1.11D,
                        new BankAccount(1L, "source firstname", "source lastname", 1.10D),
                        new BankAccount(2L, "target firstname", "target lastname", 1.10D)
                )
        );
    }

    @Test
    void transferRejectsSourceAccountNull() {
        //GIVEN
        Account source = null;
        Account target = new BankAccount(2L, "target firstname", "target lastname", 1.10D);
        double amount = 1;

        //WHEN
        //THEN
        assertThatThrownBy(() -> accountService.transfer(source, target, amount))
                .isInstanceOf(AccountIsNullException.class);
    }

    @Test
    void transferRejectsTargetAccountNull() {
        //GIVEN
        Account source = new BankAccount(2L, "target firstname", "target lastname", 1.10D);
        Account target = null;
        double amount = 1;

        //WHEN
        //THEN
        assertThatThrownBy(() -> accountService.transfer(source, target, amount))
                .isInstanceOf(AccountIsNullException.class);
    }

    @Test
    void transferRejectsSourceAccountIsNotBankAccount() {
        //GIVEN
        Account source = new Account(AccountKey.valueOf(1L), "", "", 1.10D);
        Account target = new BankAccount(2L, "", "", 1.10D);
        double amount = 1;

        //WHEN
        //THEN
        assertThatThrownBy(() -> accountService.transfer(source, target, amount))
                .isInstanceOf(AccountIsNotBankAccountException.class);
    }

    @Test
    void transferRejectsTargetAccountIsNotBankAccount() {
        //GIVEN
        Account source = new BankAccount(2L, "", "", 1.10D);
        Account target = new Account(AccountKey.valueOf(1L), "", "", 1.10D);
        double amount = 1;

        //WHEN
        //THEN
        assertThatThrownBy(() -> accountService.transfer(source, target, amount))
                .isInstanceOf(AccountIsNotBankAccountException.class);
    }

    @Test
    void transferRejectsInvalidAmount() {
        //GIVEN
        Account source = new BankAccount(2L, "", "", 1.10D);
        Account target = new BankAccount(1L, "", "", 1.10D);
        double amount = 0D;

        //WHEN
        //THEN
        assertThatThrownBy(() -> accountService.transfer(source, target, amount))
                .isInstanceOf(InvalidAmountException.class);
    }
}