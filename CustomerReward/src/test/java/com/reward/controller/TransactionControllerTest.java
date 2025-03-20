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
        Transaction transaction = new Transaction();
        when(transactionService.addTransaction(transactionDTO)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.addTransaction(transactionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).addTransaction(transactionDTO);
    }

    @Test
    public void testGetTransactions() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 31);
        List<Transaction> transactions = Arrays.asList(new Transaction());
        when(transactionService.getTransactions(customerId, startDate, endDate)).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response = transactionController.getTransactions(customerId, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).getTransactions(customerId, startDate, endDate);
    }

    @Test
    public void testUpdateTransaction() {
        Long transactionId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setAmount(transactionDTO.getAmount());
        updatedTransaction.setDate(transactionDTO.getDate());
        when(transactionService.updateTransaction(transactionId, updatedTransaction)).thenReturn(updatedTransaction);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(transactionId, transactionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTransaction, response.getBody());
        verify(transactionService, times(1)).updateTransaction(transactionId, updatedTransaction);
    }

    @Test
    public void testDeleteTransaction() {
        Long transactionId = 1L;
        doNothing().when(transactionService).deleteTransaction(transactionId);

        ResponseEntity<String> response = transactionController.deleteTransaction(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction with ID " + transactionId + " was successfully deleted.", response.getBody());
        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }
}