package com.teya.tiny_ledger.controller;

import com.teya.tiny_ledger.dao.Transaction;
import com.teya.tiny_ledger.dto.BalanceDto;
import com.teya.tiny_ledger.dto.TransactionDto;
import com.teya.tiny_ledger.dto.TransactionHistoryDto;
import com.teya.tiny_ledger.dto.TransactionType;
import com.teya.tiny_ledger.service.AccountNotFoundException;
import com.teya.tiny_ledger.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    private static final String DUMMY_ID = "aaaaaaaa-aaaa-4aaa-aaaa-aaaaaaaaaaaa";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    private UUID accountId;
    private UUID transactionId;
    private UUID reference;

    @BeforeEach
    void init() {
        accountId = UUID.randomUUID();
        transactionId = UUID.randomUUID();
        reference = UUID.randomUUID();
    }

    @Test
    void getBalance() throws Exception {

        BalanceDto balanceDto = new BalanceDto(BigDecimal.valueOf(23.99));

        given(accountService.getBalance(accountId)).willReturn(balanceDto);

        String expected = "{\"amount\":23.99}";

        MvcResult result =mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/accounts/"+ accountId.toString()+"/balance")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();

        assertNotNull(json);
        assertEquals(expected, json);

    }

    @Test
    void getBalanceErrorWhenAccountNotFound() throws Exception {
        given(accountService.getBalance(accountId)).willThrow(new AccountNotFoundException());
        String expected = "{\"message\":\"Account not found\"}";

        MvcResult result =mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/accounts/"+ accountId.toString()+"/balance")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        String json = result.getResponse().getContentAsString();

        assertNotNull(json);
        assertEquals(expected, json);
    }

    @Test
    void createTransaction() throws Exception {
        UUID reference = UUID.randomUUID();
        TransactionDto input = new TransactionDto(null, reference, TransactionType.DEPOSIT, BigDecimal.valueOf(25.56), "Test");
        String expected = """
            {"reference":"%s","type":"DEPOSIT","amount":25.56,"description":"Test"}
        """.strip().formatted(reference.toString());

        given(accountService.addTransaction(accountId,input)).willReturn(input);
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/accounts/"+ accountId +"/transactions")
                                .content(
                                        """
                                                {"reference":"%s","type":"DEPOSIT","amount":25.56,"description":"Test"}
                                        """.formatted(reference.toString())
                                )
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();

        assertNotNull(json);
        assertEquals(expected, json);
    }

    @Test
    void createTransactionErrorWhenAccountNotFound() throws Exception {
        TransactionDto input = new TransactionDto(null, reference, TransactionType.DEPOSIT, BigDecimal.valueOf(25.56), "Test");
        given(accountService.addTransaction(accountId,input)).willThrow(new AccountNotFoundException());
        String expected = "{\"message\":\"Account not found\"}";

        MvcResult result =mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/accounts/"+ accountId +"/transactions")
                                .content(
                                        """
                                                {"reference":"%s","type":"DEPOSIT","amount":25.56,"description":"Test"}
                                        """.formatted(reference.toString())
                                )
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        String json = result.getResponse().getContentAsString();

        assertNotNull(json);
        assertEquals(expected, json);
    }

    @Test
    void getTransactions() throws Exception {

        TransactionHistoryDto input = new TransactionHistoryDto(
                List.of(
                        new TransactionDto(transactionId, null, TransactionType.WITHDRAWAL, BigDecimal.valueOf(10.99), "Transaction2")
                )
        );
        given(accountService.getTransactionHistory(accountId)).willReturn(input);

        String expected = """
                {"transactions":[{"id":"%s","type":"WITHDRAWAL","amount":10.99,"description":"Transaction2"}]}        
                """.strip().formatted(transactionId);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/accounts/"+ accountId +"/transactions")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();

        assertNotNull(json);
        assertEquals(expected, json);

    }

    @Test
    void getTransactionsErrorWhenAccountNotFound() throws Exception {
        given(accountService.getTransactionHistory(accountId)).willThrow(new AccountNotFoundException());
        String expected = "{\"message\":\"Account not found\"}";

        MvcResult result =mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/accounts/"+ accountId +"/transactions")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        String json = result.getResponse().getContentAsString();

        assertNotNull(json);
        assertEquals(expected, json);
    }
}