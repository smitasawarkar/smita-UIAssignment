package com.reward.controller;

import com.reward.Service.TransactionService;
import com.reward.dto.TransactionDTO;
import com.reward.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTransaction() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setCustomerId(1L);
        transactionDTO.setAmount(100.0);
        transactionDTO.setDate(LocalDate.now());

        Transaction transaction = new Transaction();
        transaction.setCustomerId(1L);
        transaction.setAmount(100.0);
        transaction.setDate(LocalDate.now());

        when(transactionService.addTransaction(any(Transaction.class))).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.addTransaction(transactionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    @Test
    public void testGetTransactions() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();

        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(customerId);
        transaction1.setAmount(100.0);
        transaction1.setDate(LocalDate.now().minusDays(5));

        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(customerId);
        transaction2.setAmount(200.0);
        transaction2.setDate(LocalDate.now().minusDays(3));

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionService.getTransactions(customerId, startDate, endDate)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getTransactions(customerId, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    public void testUpdateTransaction() {
        Long id = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(150.0);
        transactionDTO.setDate(LocalDate.now());

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setAmount(150.0);
        updatedTransaction.setDate(LocalDate.now());

        when(transactionService.updateTransaction(eq(id), any(Transaction.class))).thenReturn(updatedTransaction);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(id, transactionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTransaction, response.getBody());
    }

    @Test
    public void testDeleteTransaction() {
        Long id = 1L;

        doNothing().when(transactionService).deleteTransaction(id);

        ResponseEntity<String> response = transactionController.deleteTransaction(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction with ID " + id + " was successfully deleted.", response.getBody());
    }
}