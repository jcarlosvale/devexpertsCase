package com.devexperts.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devexperts.model.BankAccount;
import com.devexperts.model.BankAccountKey;
import com.devexperts.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private static final long SOURCE_ID = 1L;
    private static final long TARGET_ID = 2L;

    @SpyBean
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validTransfer() throws Exception {
        //GIVEN
        final double amount = 99.99;
        final BankAccount sourceBankAccount = new BankAccount(SOURCE_ID, "", "", 100.00);
        final BankAccount targetBankAccount = new BankAccount(TARGET_ID, "", "", 0.00);

        given(accountRepository.retrieve(BankAccountKey.valueOf(SOURCE_ID)))
                .willReturn(sourceBankAccount);
        given(accountRepository.retrieve(BankAccountKey.valueOf(TARGET_ID)))
                .willReturn(targetBankAccount);

        //WHEN
        mockMvc.perform(post("/api/operations/transfer")
                        .param("sourceId", SOURCE_ID + "")
                        .param("targetId", TARGET_ID + "")
                        .param("amount", amount + ""))
                .andExpect(status().isOk());

        //THEN
        assertThat(sourceBankAccount.getBalance()).isEqualTo(0.01);
        assertThat(targetBankAccount.getBalance()).isEqualTo(99.99);
    }

    @Test
    void invalidAmountTransfer() throws Exception {
        //GIVEN
        final double amount = 0.00;
        final BankAccount sourceBankAccount = new BankAccount(SOURCE_ID, "", "", 100.00);
        final BankAccount targetBankAccount = new BankAccount(TARGET_ID, "", "", 0.00);

        given(accountRepository.retrieve(BankAccountKey.valueOf(SOURCE_ID)))
                .willReturn(sourceBankAccount);
        given(accountRepository.retrieve(BankAccountKey.valueOf(TARGET_ID)))
                .willReturn(targetBankAccount);

        //WHEN
        mockMvc.perform(post("/api/operations/transfer")
                        .param("sourceId", SOURCE_ID + "")
                        .param("targetId", TARGET_ID + "")
                        .param("amount", amount + ""))
                .andExpect(status().isBadRequest());

        //THEN
        assertThat(sourceBankAccount.getBalance()).isEqualTo(100.00);
        assertThat(targetBankAccount.getBalance()).isEqualTo(0.00);
    }

    @Test
    void sourceAccountNotFoundTransfer() throws Exception {
        //GIVEN
        final double amount = 0.01;
        final BankAccount targetBankAccount = new BankAccount(TARGET_ID, "", "", 0.00);

        given(accountRepository.retrieve(BankAccountKey.valueOf(TARGET_ID)))
                .willReturn(targetBankAccount);

        //WHEN
        mockMvc.perform(post("/api/operations/transfer")
                        .param("sourceId", SOURCE_ID + "")
                        .param("targetId", TARGET_ID + "")
                        .param("amount", amount + ""))
                .andExpect(status().isNotFound());

        //THEN
        assertThat(targetBankAccount.getBalance()).isEqualTo(0.00);
    }

    @Test
    void targetAccountNotFoundTransfer() throws Exception {
        //GIVEN
        final double amount = 0.01;
        final BankAccount sourceBankAccount = new BankAccount(SOURCE_ID, "", "", 0.01);

        given(accountRepository.retrieve(BankAccountKey.valueOf(SOURCE_ID)))
                .willReturn(sourceBankAccount);

        //WHEN
        mockMvc.perform(post("/api/operations/transfer")
                        .param("sourceId", SOURCE_ID + "")
                        .param("targetId", TARGET_ID + "")
                        .param("amount", amount + ""))
                .andExpect(status().isNotFound());

        //THEN
        assertThat(sourceBankAccount.getBalance()).isEqualTo(0.01);
    }

    @Test
    void invalidTransfer() throws Exception {
        //GIVEN
        final double amount = 100.01;
        final BankAccount sourceBankAccount = new BankAccount(SOURCE_ID, "", "", 100.00);
        final BankAccount targetBankAccount = new BankAccount(TARGET_ID, "", "", 0.00);

        given(accountRepository.retrieve(BankAccountKey.valueOf(SOURCE_ID)))
                .willReturn(sourceBankAccount);
        given(accountRepository.retrieve(BankAccountKey.valueOf(TARGET_ID)))
                .willReturn(targetBankAccount);

        //WHEN
        mockMvc.perform(post("/api/operations/transfer")
                        .param("sourceId", SOURCE_ID + "")
                        .param("targetId", TARGET_ID + "")
                        .param("amount", amount + ""))
                .andExpect(status().isInternalServerError());

        //THEN
        assertThat(sourceBankAccount.getBalance()).isEqualTo(100.00);
        assertThat(targetBankAccount.getBalance()).isEqualTo(0.00);
    }
}