package com.reward.service;
import com.reward.Service.TransactionService;
import com.reward.dto.TransactionDTO;
import com.reward.entity.Customer;
import com.reward.entity.Transaction;
import com.reward.exception.ResourceNotFoundException;
import com.reward.repository.CustomerRepository;
import com.reward.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CustomerRepository customerRepository;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTransaction() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setCustomerId(1L);
        transactionDTO.setAmount(100.0);
        transactionDTO.setDate(LocalDate.of(2025, 3, 1));

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(transactionDTO.getCustomerId())).thenReturn(Optional.of(customer));

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDate(transactionDTO.getDate());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.addTransaction(transactionDTO);

        assertEquals(transaction, result);
        verify(customerRepository, times(1)).findById(transactionDTO.getCustomerId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testAddTransactionCustomerNotFound() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setCustomerId(1L);

        when(customerRepository.findById(transactionDTO.getCustomerId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.addTransaction(transactionDTO);
        });

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(transactionDTO.getCustomerId());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testGetTransactions() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 31);
        List<Transaction> transactions = Arrays.asList(new Transaction());

        when(transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactions(customerId, startDate, endDate);

        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findByCustomerIdAndDateBetween(customerId, startDate, endDate);
    }

    @Test
    public void testGetTransactionsNoTransactionsFound() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 31);

        when(transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactions(customerId, startDate, endDate);
        });

        assertEquals("No transactions found for the given criteria.", exception.getMessage());
        verify(transactionRepository, times(1)).findByCustomerIdAndDateBetween(customerId, startDate, endDate);
    }

    @Test
    public void testUpdateTransaction() {
        Long transactionId = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setAmount(200.0);
        updatedTransaction.setDate(LocalDate.of(2025, 3, 2));

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(transactionId);
        existingTransaction.setAmount(100.0);
        existingTransaction.setDate(LocalDate.of(2025, 3, 1));

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        Transaction result = transactionService.updateTransaction(transactionId, updatedTransaction);

        assertEquals(updatedTransaction, result);
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testUpdateTransactionNotFound() {
        Long transactionId = 1L;
        Transaction updatedTransaction = new Transaction();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.updateTransaction(transactionId, updatedTransaction);
        });

        assertEquals("Transaction not found with ID " + transactionId, exception.getMessage());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).delete(transaction);

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    public void testDeleteTransactionNotFound() {
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.deleteTransaction(transactionId);
        });

        assertEquals("Transaction not found with ID " + transactionId, exception.getMessage());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }
}