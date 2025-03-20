package com.reward.service;

import com.reward.Service.TransactionService;
import com.reward.entity.Transaction;
import com.reward.exception.ResourceNotFoundException;
import com.reward.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTransaction() {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(1L);
        transaction.setAmount(100.0);
        transaction.setDate(LocalDate.now());

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.addTransaction(transaction);

        assertEquals(transaction, result);
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

        when(transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactions(customerId, startDate, endDate);

        assertEquals(transactions, result);
    }

    @Test
    public void testUpdateTransaction() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setAmount(150.0);
        updatedTransaction.setDate(LocalDate.now());

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(id);
        existingTransaction.setCustomerId(1L);
        existingTransaction.setAmount(100.0);
        existingTransaction.setDate(LocalDate.now().minusDays(5));

        when(transactionRepository.findById(id)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(existingTransaction)).thenReturn(existingTransaction);

        Transaction result = transactionService.updateTransaction(id, updatedTransaction);

        assertEquals(updatedTransaction.getAmount(), result.getAmount());
        assertEquals(updatedTransaction.getDate(), result.getDate());
    }

    @Test
    public void testDeleteTransaction() {
        Long id = 1L;

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setCustomerId(1L);
        transaction.setAmount(100.0);
        transaction.setDate(LocalDate.now().minusDays(5));

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).delete(transaction);

        assertDoesNotThrow(() -> transactionService.deleteTransaction(id));
    }

    @Test
    public void testGetTransactionsNotFound() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();

        when(transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate)).thenReturn(Arrays.asList());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactions(customerId, startDate, endDate));
    }

    @Test
    public void testUpdateTransactionNotFound() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setAmount(150.0);
        updatedTransaction.setDate(LocalDate.now());

        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.updateTransaction(id, updatedTransaction));
    }

    @Test
    public void testDeleteTransactionNotFound() {
        Long id = 1L;

        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.deleteTransaction(id));
    }
}