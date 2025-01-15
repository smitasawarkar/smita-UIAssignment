package com.reward.Service;
import com.reward.entity.Transaction;
import com.reward.exception.ResourceNotFoundException;
import com.reward.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;
    public Transaction addTransaction(Transaction transaction) {
        logger.info("Adding transaction: {}", transaction);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(Long customerId, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching transactions for customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for the given criteria.");
        }
        return transactions;
    }


    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        logger.info("Updating transaction with id: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID " + id));
        transaction.setAmount(updatedTransaction.getAmount());
        transaction.setDate(updatedTransaction.getDate());
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        logger.info("Deleting transaction with id: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID " + id));
        transactionRepository.delete(transaction);
    }
}